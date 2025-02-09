package com.example.precisepal.presentation.signIn

import android.content.Context

sealed class SignInEvent {
    //when ever we need to pass the data also with the event, then we use data class
    data class SignInWithGoogle(val context: Context) : SignInEvent()
    //here we don't pass anything so we use data object
    data object SignInAnonymously : SignInEvent()
}