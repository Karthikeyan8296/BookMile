package com.example.precisepal.domain.repository

import com.example.precisepal.domain.model.User
import kotlinx.coroutines.flow.Flow

// here we will define all the function related to the database
interface DatabaseRepository {
    // storing the user in the database
    suspend fun addUser(): Result<Boolean>
    // get the user details from the firestore and show in UI
    // ? -> this can also be nullable
    fun getSignInUserName() : Flow<User?>
}