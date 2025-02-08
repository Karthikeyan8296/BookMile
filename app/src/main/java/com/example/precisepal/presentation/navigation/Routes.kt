package com.example.precisepal.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object SignInScreen : Routes()
    @Serializable
    data object DashboardScreen: Routes()
    @Serializable
    data object AddItemsScreen: Routes()
    //to get the data also from previous screen, so we use data class
    @Serializable
    data class DetailsScreen(val bodyPartId: String) : Routes()
}