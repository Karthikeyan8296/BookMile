package com.example.precisepal.presentation.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.precisepal.domain.repository.AuthRepository
import com.example.precisepal.domain.repository.DatabaseRepository
import com.example.precisepal.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository,
) :
    ViewModel() {
    //for snackBar event
    private val _uiEvent = Channel<UIEvent>()

    //convert the uiEvent channel to flow
    val uiEvent = _uiEvent.receiveAsFlow()

    //event
    fun onEvent(event: DashboardEvents) {
        when (event) {
            is DashboardEvents.AnonymousUserSignInWithGoogle -> {
                AnonymousUserSignInWithGoogle(context = event.context)
            }

            DashboardEvents.SignOut -> {
                signOut()
            }
        }
    }

    //state
    private val _state = MutableStateFlow(DashboardState())
    val state = combine(
        _state,
        databaseRepository.getSignInUserName(),
        databaseRepository.getAllBookPageLatestValue()
    ) { state, user, bodyParts ->
        val activeBodyParts = bodyParts.filter { it.isActive }
        state.copy(
            user = user,
            books = activeBodyParts
        )
    }.catch { e ->
        _uiEvent.send(UIEvent.ShowSnackBar("❌ Oops! Something went wrong. Please try again later. ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DashboardState()
    )

    private fun signOut() {
        viewModelScope.launch {
            _state.update { it.copy(isSignOutButtonLoading = true) }
            authRepository.signOut()
                .onSuccess {
                    _uiEvent.send(UIEvent.HideBottomSheet)
                    _uiEvent.send(UIEvent.ShowSnackBar("✅ Signed out successfully! See you soon."))
                }
                .onFailure { e ->
                    _uiEvent.send(UIEvent.HideBottomSheet)
                    _uiEvent.send(UIEvent.ShowSnackBar("❌ Sign-out failed. Please try again later. ${e.message}"))
                }
            _state.update { it.copy(isSignOutButtonLoading = false) }
        }
    }

    private fun AnonymousUserSignInWithGoogle(context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isSignInButtonLoading = true) }
            authRepository.anonymousUserSignInWithGoogle(context)
                .onSuccess {
                    //firebase user collection - storing the user in database
                    databaseRepository.addUser()
                        .onSuccess {
                            _uiEvent.send(UIEvent.HideBottomSheet)
                            _uiEvent.send(UIEvent.ShowSnackBar("✅ Sign-in successful!"))
                        }
                        .onFailure { e ->
                            _uiEvent.send(UIEvent.HideBottomSheet)
                            _uiEvent.send(UIEvent.ShowSnackBar("❌ User registration failed. Please try again later ${e.message}"))
                        }
                }
                .onFailure { e ->
                    _uiEvent.send(UIEvent.HideBottomSheet)
                    _uiEvent.send(UIEvent.ShowSnackBar("❌ Sign-in failed. Please try again later ${e.message}"))
                    Log.d("SignInViewModel", "Google signIn error: ${e.message}")
                }
            _state.update { it.copy(isSignInButtonLoading = false) }
        }
    }
}