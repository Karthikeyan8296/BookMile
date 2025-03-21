package com.example.precisepal.presentation.signIn

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.precisepal.domain.model.AuthStatus
import com.example.precisepal.domain.model.predefinedBodyPart
import com.example.precisepal.domain.repository.AuthRepository
import com.example.precisepal.domain.repository.DatabaseRepository
import com.example.precisepal.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//those state and events will be managed in the view model
//we are getting this auth interface
@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository,
) :
    ViewModel() {

    //for snackBar event
    private val _uiEvent = Channel<UIEvent>()

    //convert the uiEvent channel to flow
    val uiEvent = _uiEvent.receiveAsFlow()

    //------------------//
    //for state we will use mutableStateFlow
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    //for events we use single function
    fun onEvent(event: SignInEvent) {
        when (event) {
            SignInEvent.SignInAnonymously -> {
                signInAnonymously()
            }

            is SignInEvent.SignInWithGoogle -> {
                signInWithGoogle(context = event.context)
            }
        }
    }

    //auth status
    //converting that flow to stateflow to use in compose
    val authStatus = authRepository.authState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = AuthStatus.LOADING
        )


    //SignInAnonymously function to launch it with authRepository
    private fun signInAnonymously() {
        viewModelScope.launch {
            _state.update { it.copy(isAnonymousSignInButtonLoading = true) }
            authRepository.signInAnonymously()
                .onSuccess {
                    //Adding the Anonymous user in the database
                    databaseRepository.addUser()
                        .onSuccess {
                            _uiEvent.send(UIEvent.ShowSnackBar("Signed In Successfully, your data stored in the database"))
                        }
                        .onFailure { e ->
                            _uiEvent.send(UIEvent.ShowSnackBar("Couldn't add user${e.message}"))
                        }
                    _uiEvent.send(UIEvent.ShowSnackBar("Signed In Successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UIEvent.ShowSnackBar("Couldn't sign in. please try again later ${e.message}"))
                }
            _state.update { it.copy(isAnonymousSignInButtonLoading = false) }
        }
    }

    //SignInWithGoogle function to launch it with authRepository
    private fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isGoogleSignInButtonLoading = true) }
            authRepository.signInWithGoogle(context)
                .onSuccess { isNewUser ->
                    //firebase user collection - storing the user in database
                    if (isNewUser) {
                        databaseRepository.addUser()
                            .onSuccess {
                                //inserting the predefined values in the DB
                                try {
                                    insertPredefinedBodyPartValues()
                                    _uiEvent.send(UIEvent.ShowSnackBar("Predefined Body Part Values Inserted Successfully"))
                                } catch (e: Exception) {
                                    _uiEvent.send(UIEvent.ShowSnackBar("failed to insert in DB ${e.message}"))
                                }
                                _uiEvent.send(UIEvent.ShowSnackBar("Signed In Successfully, your data stored in the database"))
                            }
                            .onFailure { e ->
                                _uiEvent.send(UIEvent.ShowSnackBar("Couldn't add user${e.message}"))
                            }
                    } else {
                        _uiEvent.send(UIEvent.ShowSnackBar("Signed In Successfully, your data already stored in the database"))
                    }
                }
                .onFailure { e ->
                    _uiEvent.send(UIEvent.ShowSnackBar("Couldn't sign in. please try again later ${e.message}"))
                    Log.d("SignInViewModel", "Google signIn error: ${e.message}")
                }
            _state.update { it.copy(isGoogleSignInButtonLoading = false) }
        }
    }

    private suspend fun insertPredefinedBodyPartValues() {
        predefinedBodyPart.forEach { bodyPart ->
            databaseRepository.upsertBodyPort(bodyPart)
        }
    }
}