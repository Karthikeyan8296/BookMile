package com.example.precisepal.presentation.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.precisepal.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//those state and events will be managed in the view model
//we are getting this auth interface
class SignInViewModel @Inject constructor (private val authRepository: AuthRepository) : ViewModel() {

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

            }
        }
    }
    private fun signInAnonymously(){
        viewModelScope.launch {
            authRepository.signInAnonymously()
                .onSuccess {

                }
                .onFailure {

                }
        }
    }
}