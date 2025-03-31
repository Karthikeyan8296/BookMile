package com.example.precisepal.presentation.dashboard

import com.example.precisepal.domain.model.Book
import com.example.precisepal.domain.model.User

data class DashboardState(
    val user: User? = null,
    val books: List<Book> = emptyList(),
    val isSignInButtonLoading: Boolean = false,
    val isSignOutButtonLoading: Boolean = false
)
