package com.example.precisepal.presentation.signIn

data class SignInState(
    //for states we always need to keep some default values
    val isGoogleSignInButtonLoading: Boolean = false,
    val isAnonymousSignInButtonLoading: Boolean = false
)
