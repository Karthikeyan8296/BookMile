package com.example.precisepal.presentation.dashboard

import android.content.Context

sealed class DashboardEvents {
    data class AnonymousUserSignInWithGoogle(val context: Context) : DashboardEvents()
    data object SignOut: DashboardEvents()
}