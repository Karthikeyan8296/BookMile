package com.example.precisepal.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.example.precisepal.data.repository.AuthRepositoryImpl
import com.example.precisepal.data.repository.DatabaseRepositoryImpl
import com.example.precisepal.domain.repository.AuthRepository
import com.example.precisepal.domain.repository.DatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    //we use provide as prefix to the function
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        credentialManager: CredentialManager,
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, credentialManager)
    }

    //google auth
    @Provides
    @Singleton
    fun provideCredentialManager(
        @ApplicationContext context: Context,
    ): CredentialManager {
        return CredentialManager.create(context)
    }

    //firebase Anonymously auth
    //val firebaseAuth = FirebaseAuth.getInstance()
    //for creating multiple instance of object like this, we use dagger hilt to make it easy and call anywhere!
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    //fire base firestore
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    //we use provide as prefix to the function
    fun provideDataBaseRepository(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore): DatabaseRepository {
        return DatabaseRepositoryImpl(firebaseAuth, firestore)
    }
}