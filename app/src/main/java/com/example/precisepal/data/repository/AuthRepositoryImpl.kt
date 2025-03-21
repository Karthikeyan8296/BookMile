package com.example.precisepal.data.repository

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.precisepal.data.util.constants
import com.example.precisepal.domain.model.AuthStatus
import com.example.precisepal.domain.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager,
) : AuthRepository {

    //auth status
    override val authState: Flow<AuthStatus> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val status = if (auth.currentUser != null) {
                AuthStatus.AUTHENTICATED
            } else AuthStatus.UNAUTHENTICATED
            trySend(status)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.distinctUntilChanged()

    //sign out
    override suspend fun signOut(): Result<Boolean> {
        return try {
            firebaseAuth.signOut()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //sign in Anonymously
    override suspend fun signInAnonymously(): Result<Boolean> {
        return try {
            firebaseAuth.signInAnonymously().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Anonymous user sign in with google function
    override suspend fun anonymousUserSignInWithGoogle(context: Context): Result<Boolean> {
        return try {
            val authCredentialResult = getGoogleAuthCredentials(context)
//            sign-in with google firebase setup
            if (authCredentialResult.isSuccess) {
                val authCredential = authCredentialResult.getOrNull()
                if (authCredential != null) {
//                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()
//                    //check if the user is New or Not
//                    val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
                    firebaseAuth.currentUser?.linkWithCredential(authCredential)?.await()
                    Result.success(true)
                } else {
                    Result.failure(IllegalArgumentException("authCredential is null"))
                }
            } else {
                Result.failure(authCredentialResult.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //sign in with google
    override suspend fun signInWithGoogle(context: Context): Result<Boolean> {
        return try {
            val authCredentialResult = getGoogleAuthCredentials(context)
//            sign-in with google firebase setup
            if (authCredentialResult.isSuccess) {
                val authCredential = authCredentialResult.getOrNull()
                if (authCredential != null) {
                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                    //check if the user is New or Not
                    val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
                    Result.success(isNewUser)
                } else {
                    Result.failure(IllegalArgumentException("authCredential is null"))
                }
            } else {
                Result.failure(authCredentialResult.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //google signIn builder(popup or bottom sheet)
    private suspend fun getGoogleAuthCredentials(context: Context): Result<AuthCredential?> {
        return try {
            //some random unique number used once
            val nonce = UUID.randomUUID().toString()
            Log.d(constants.APP_LOG, "Nonce: $nonce")
            //google option popup
            val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption
                .Builder(serverClientId = constants.GOOGLE_CLIENT_WEB_ID)
                .setNonce(nonce)
                .build()
            //request
            val request: GetCredentialRequest = GetCredentialRequest
                .Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()
            //credentials
            //pass the credential manager object
            val credential =
                credentialManager.getCredential(context = context, request = request).credential
            //json google id token - json web token
            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
            val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            Log.d(constants.APP_LOG, "Credential: $credential")
            Log.d(constants.APP_LOG, "googleIdToken: $googleIdToken")
            Log.d(constants.APP_LOG, "authCredential: $authCredential")
            Result.success(authCredential)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}