package com.example.precisepal.presentation.dashboard

import com.example.precisepal.domain.model.BodyPart
import com.example.precisepal.domain.model.User

data class DashboardState(
    val user: User? = null,
    val bodyParts: List<BodyPart> = emptyList(),
    val isSignInButtonLoading: Boolean = false,
    val isSignOutButtonLoading: Boolean = false
)
