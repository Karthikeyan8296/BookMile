package com.example.precisepal.data.repository

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import com.example.precisepal.domain.repository.AuthRepository

class AuthRepositoryImpl: AuthRepository {
    override suspend fun signInAnonymously(): Result<Boolean> {
        return try {
            Result.success(true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}