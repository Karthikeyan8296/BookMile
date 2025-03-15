package com.example.precisepal.data.mapper

import com.example.precisepal.domain.model.User

//From firestore we will get these data
//we need to set default values too, for firebase firestore, otherwise it will throw some error
data class UserDTO(
    val name: String = "Anonymous",
    val email: String = "Anonymous@gmail.com",
    val profilePic: String = "",
    val anonymous: Boolean = true,
    val userID: String? = null,
)

//will need convert userDTO to user
//mapper function
fun UserDTO.toUser(): User {
    return User(
        name = name,
        email = email,
        profilePic = profilePic,
        isAnonymous = anonymous,
        userID = userID,
    )
}