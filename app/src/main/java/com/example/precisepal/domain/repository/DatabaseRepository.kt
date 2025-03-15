package com.example.precisepal.domain.repository

//here we will define all the function related to the database
interface DatabaseRepository {
    //storing the user in the database
    suspend fun addUser(): Result<Boolean>
}