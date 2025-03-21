package com.example.precisepal.domain.repository

import android.content.Context
import com.example.precisepal.domain.model.AuthStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

//here we will declare all the functions related to authentication
interface AuthRepository {
    //to navigate to home, after login
    val authState: Flow<AuthStatus>

    //function behaviour
    suspend fun signInAnonymously(): Result<Boolean>
    suspend fun anonymousUserSignInWithGoogle(context: Context): Result<Boolean>
    suspend fun signInWithGoogle(context: Context): Result<Boolean>
    suspend fun signOut(): Result<Boolean>
}