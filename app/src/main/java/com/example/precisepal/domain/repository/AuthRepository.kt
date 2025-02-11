package com.example.precisepal.domain.repository

//here we will define all the functions related to authentication
interface AuthRepository {
    suspend fun signInAnonymously() : Result<Boolean>
}