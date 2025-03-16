package com.example.precisepal.data.repository

import com.example.precisepal.data.mapper.UserDTO
import com.example.precisepal.data.mapper.toUser
import com.example.precisepal.data.util.constants.USERS_COLLECTION
import com.example.precisepal.domain.model.User
import com.example.precisepal.domain.repository.DatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DatabaseRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : DatabaseRepository {

    //firebase user collection - creating an collection named "user"
    private fun userCollection(): CollectionReference {
        return firestore.collection(USERS_COLLECTION)
    }

    //storing the user in the database
    override suspend fun addUser(): Result<Boolean> {
        return try {
            val firebaseUser = firebaseAuth.currentUser
                ?: throw IllegalArgumentException("No current user Logged in")
            var userDTO = UserDTO(
                userID = firebaseUser.uid,
                //already we have provided the default values
//                name = "Anonymous",
//                email = "Anonymous@gmail.com",
//                profilePic = "",
                anonymous = firebaseUser.isAnonymous
            )
            firebaseUser.providerData.forEach { profile ->
                userDTO = userDTO.copy(
                    name = profile.displayName ?: userDTO.name,
                    email = profile.email ?: userDTO.email,
                    profilePic = profile.photoUrl?.toString() ?: userDTO.profilePic,
                )
            }
            userCollection()
                .document(firebaseUser.uid)
                .set(userDTO)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //getting the user names from the firestore and show in UI, from the user mapper function
    override fun getSignInUserName(): Flow<User?> {
        return flow{
            try {
                val userID = firebaseAuth.currentUser?.uid ?: ""
                userCollection()
                    .document(userID)
                    .snapshots()
                    .collect{ snapshot ->
                        val userDTO = snapshot.toObject(UserDTO::class.java)
                        emit(userDTO?.toUser())
                    }
            }
            catch (e:Exception){
                throw e
            }
        }
    }
}