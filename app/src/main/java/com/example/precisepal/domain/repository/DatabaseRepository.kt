package com.example.precisepal.domain.repository

import com.example.precisepal.domain.model.BodyPart
import com.example.precisepal.domain.model.User
import kotlinx.coroutines.flow.Flow

// here we will define all the function related to the database
interface DatabaseRepository {
    // storing the user in the database
    suspend fun addUser(): Result<Boolean>
    // get the user details from the firestore and show in UI
    // ? -> this can also be nullable
    fun getSignInUserName() : Flow<User?>

    //upsert - It is the combination of insert and update
    //insert and update the body part in the database
    suspend fun upsertBodyPort(bodyPart: BodyPart) : Result<Boolean>
}