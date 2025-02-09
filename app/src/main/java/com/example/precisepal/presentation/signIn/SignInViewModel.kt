package com.example.precisepal.presentation.signIn

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//those state and events will be managed in the view model
class SignInViewModel : ViewModel() {

    //for state we will use mutableStateFlow
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    //for events we use single function
    fun onEvent(event: SignInEvent){
        when(event){
            SignInEvent.SignInAnonymously -> {

            }
            is SignInEvent.SignInWithGoogle -> {


            }
        }
    }
}