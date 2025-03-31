package com.example.precisepal.data.repository

import com.example.precisepal.data.mapper.BodyPartDTO
import com.example.precisepal.data.mapper.BodyPartValueDTO
import com.example.precisepal.data.mapper.UserDTO
import com.example.precisepal.data.mapper.toBodyPart
import com.example.precisepal.data.mapper.toBodyPartDTO
import com.example.precisepal.data.mapper.toBodyPartValueDTO
import com.example.precisepal.data.mapper.toBodyPartValues
import com.example.precisepal.data.mapper.toUser
import com.example.precisepal.data.util.constants.BODY_PART_COLLECTION
import com.example.precisepal.data.util.constants.BODY_PART_NAME_FIELD
import com.example.precisepal.data.util.constants.BODY_PART_VALUES_COLLECTION
import com.example.precisepal.data.util.constants.BODY_PART_VALUE_DATE_FIELD
import com.example.precisepal.data.util.constants.USERS_COLLECTION
import com.example.precisepal.domain.model.BodyPart
import com.example.precisepal.domain.model.BodyPartValues
import com.example.precisepal.domain.model.User
import com.example.precisepal.domain.repository.DatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlin.contracts.Returns

class DatabaseRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
) : DatabaseRepository {

    //firebase user collection - creating an collection named "user"
    private fun userCollection(): CollectionReference {
        return firebaseFireStore.collection(USERS_COLLECTION)
    }

    //firebase body part collection - creating an collection named "bodyPart"
    private fun bodyPartCollection(
        userID: String = firebaseAuth.currentUser?.uid.orEmpty(),
    ): CollectionReference {
        return firebaseFireStore.collection(USERS_COLLECTION)
            .document(userID)
            .collection(BODY_PART_COLLECTION)
    }

    //firebase body part value collection - creating an collection named "bodyPartValue"
    private fun bodyPartValueCollection(
        bodyPartID: String,
        userID: String = firebaseAuth.currentUser?.uid.orEmpty(),
    ): CollectionReference {
        return firebaseFireStore
            .collection(USERS_COLLECTION)
            .document(userID)
            .collection(BODY_PART_COLLECTION)
            .document(bodyPartID)
            .collection(BODY_PART_VALUES_COLLECTION)
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
        return flow {
            try {
                val userID = firebaseAuth.currentUser?.uid ?: ""
                userCollection()
                    .document(userID)
                    .snapshots()
                    .collect { snapshot ->
                        val userDTO = snapshot.toObject(UserDTO::class.java)
                        emit(userDTO?.toUser())
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    //insert and update the body part in the database
    override suspend fun upsertBodyPort(bodyPart: BodyPart): Result<Boolean> {
        return try {
            //if we edit the existing body part, then it will edit that id only, will not create new one
            val documentID = bodyPart.bodyPartId ?: bodyPartCollection().document().id
            //we are setting the bodypart id to the bodypartDTO as Document ID
            val bodyPartDTO = bodyPart.toBodyPartDTO().copy(bodyPartId = documentID)
            bodyPartCollection()
                .document(documentID)
                .set(bodyPartDTO)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    //get the bodyParts from the DB
    override fun getAllBodyParts(): Flow<List<BodyPart>> {
        return flow {
            try {
                bodyPartCollection()
                    .orderBy(BODY_PART_NAME_FIELD)
                    .snapshots()
                    .collect { snapshot ->
                        val bodyPartDTOList = snapshot.toObjects(BodyPartDTO::class.java)
                        emit(bodyPartDTOList.map { it.toBodyPart() })
                        // Debugging: Log sorted names
//                        Log.d("Firestore", "Fetched and sorted body parts:")
//                        bodyPartDTOList.forEach { Log.d("Firestore", "BodyPart: ${it.name}") }
//                        bodyPartDTOList.forEach { Log.d("Firestore", "BodyPart: $it") }

                    }
//                bodyPartCollection().snapshots().collect { snapshot ->
//                    Log.d("Firestore", "Fetched documents: ${snapshot.documents}")
//                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    //get the specific bodyPart for details screen
    override fun getBodyPart(bodyPartId: String): Flow<BodyPart?> {
        return flow {
            try {
                bodyPartCollection()
                    .document(bodyPartId)
                    .snapshots()
                    .collect { snapshot ->
                        val bodyPartDTO = snapshot.toObject(BodyPartDTO::class.java)
                        emit(bodyPartDTO?.toBodyPart())
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    //delete the body part from details screen
    override suspend fun deleteBodyPart(bodyPartID: String): Result<Boolean> {
        return try {
            bodyPartCollection()
                .document(bodyPartID)
                .delete()
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //update and insert the body part values
    override suspend fun upsertBodyPartValues(bodyPartValues: BodyPartValues): Result<Boolean> {
        return try {
            val bodyPartValueCollection =
                bodyPartValueCollection(bodyPartValues.bodyPartId.orEmpty())
            val documentID = bodyPartValues.bodyPartValueID ?: bodyPartValueCollection.document().id
            val bodyPartValueDTO =
                bodyPartValues.toBodyPartValueDTO().copy(bodyPartValueID = documentID)
            bodyPartValueCollection
                .document(documentID)
                .set(bodyPartValueDTO)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //get the bodyPartValues from the firestore
    override fun getAllBodyPartValues(bodyPartId: String): Flow<List<BodyPartValues>> {
        return flow {
            try {
                bodyPartValueCollection(bodyPartId)
                    .orderBy(BODY_PART_VALUE_DATE_FIELD, Query.Direction.DESCENDING)
                    .snapshots()
                    .collect { snapshot ->
                        val bodyPartValueDTOList = snapshot.toObjects(BodyPartValueDTO::class.java)
                        emit(bodyPartValueDTOList.map { it.toBodyPartValues() })
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    //delete bodyPart value
    override suspend fun deleteBodyPartValue(bodyPartValues: BodyPartValues): Result<Boolean> {
        return try {
            bodyPartValueCollection(bodyPartValues.bodyPartId.orEmpty())
                .document(bodyPartValues.bodyPartValueID.orEmpty())
                .delete()
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //get all bodyParts latest value to show in the dashboard screen
    override fun getAllBodyPartLatestValue(): Flow<List<BodyPart>> {
        return flow {
            try {
                bodyPartCollection()
                    .orderBy(BODY_PART_NAME_FIELD)
                    .snapshots()
                    .collect { snapshot ->
                        val bodyPartDTOList = snapshot.toObjects(BodyPartDTO::class.java)
                        val bodyPart = bodyPartDTOList.mapNotNull { bodyPartDTO ->
                            bodyPartDTO.bodyPartId?.let { bodyPartID ->
                                val latestBodyPartValue = getLatestBodyPartValue(bodyPartID)
                                bodyPartDTO.copy(latestValue = latestBodyPartValue?.value ?: 0.0f)
                                    .toBodyPart()
                            }
                        }
                        emit(bodyPart)
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }


    private suspend fun getLatestBodyPartValue(bodyPartID: String): BodyPartValueDTO? {
        val querySnapshot = bodyPartValueCollection(bodyPartID)
            .orderBy(BODY_PART_VALUE_DATE_FIELD, Query.Direction.DESCENDING)
            .limit(1)
            .snapshots()
            .firstOrNull()
        return querySnapshot?.documents?.firstOrNull()?.toObject(BodyPartValueDTO::class.java)
    }
}