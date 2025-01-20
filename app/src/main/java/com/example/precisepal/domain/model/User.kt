package com.example.precisepal.domain.model

data class User(
    val name: String,
    val email: String,
    val profilePic: String,
    val isAnonymous: Boolean,
    val userID: String? = null,
)
