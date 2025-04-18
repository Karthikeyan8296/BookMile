package com.example.precisepal.domain.repository

import com.example.precisepal.domain.model.Book
import com.example.precisepal.domain.model.BookDetails
import com.example.precisepal.domain.model.User
import kotlinx.coroutines.flow.Flow

// here we will define all the function related to the database
//suspend function - used for one time actions
//normal function - for the continuous flow of action
interface DatabaseRepository {
    // storing the user in the database
    suspend fun addUser(): Result<Boolean>

    // get the user details from the firestore and show in UI
    // ? -> this can also be nullable
    fun getSignInUserName(): Flow<User?>

    //upsert - It is the combination of insert and update
    //insert and update the book  in the database
    suspend fun upsertBook(bodyPart: Book): Result<Boolean>

    //get all Books from the firestore DB
    fun getAllBooks(): Flow<List<Book>>

    //get the Book for the details screen
    fun getBook(bodyPartId: String): Flow<Book?>

    //delete the book from the firestore DB
    suspend fun deleteBook(bodyPartID: String): Result<Boolean>

    //send Book details page values to firestore - graph values
    suspend fun upsertBookPageValues(bodyPartValues: BookDetails): Result<Boolean>

    //get all the book page values from the firestore
    fun getAllBookPageValues(bodyPartId: String): Flow<List<BookDetails>>

    //delete Book page values from the firestore DB
    suspend fun deleteBookPageValue(bodyPartValues: BookDetails): Result<Boolean>

    //get all books page latest value to show in the dashboard screen
    fun getAllBookPageLatestValue(): Flow<List<Book>>
}