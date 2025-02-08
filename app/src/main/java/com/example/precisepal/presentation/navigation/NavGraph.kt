package com.example.precisepal.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.precisepal.presentation.addItems.AddItemsScreen
import com.example.precisepal.presentation.dashboard.DashboardScreen
import com.example.precisepal.presentation.details.DetailsScreen
import com.example.precisepal.presentation.signIn.SignInScreen

@SuppressLint("NewApi")
@Composable
fun NavGraph(
    navControllerInstance: NavHostController,
    windowSize: WindowSizeClass,
    paddingValuesInstance: PaddingValues,
) {
    NavHost(
        navController = navControllerInstance,
        //here we are telling, from where our app will starts
        startDestination = Routes.DashboardScreen
    )
    {
        //for which screen we will use this composable block
        composable<Routes.SignInScreen> {
            SignInScreen(
                windowSizeInstance = windowSize.widthSizeClass,
                paddingValuesInstance = paddingValuesInstance
            )
        }

        composable<Routes.DashboardScreen> {
            DashboardScreen(
                onFabClick = { navControllerInstance.navigate(Routes.AddItemsScreen) },
                //We are passing the bodyPartID
                //In that lambda function we are passing that string
                onItemCardClicked = { bodyPartID ->
                    navControllerInstance.navigate(Routes.DetailsScreen(bodyPartID))
                },
                paddingValuesInstance = paddingValuesInstance
            )
        }

        composable<Routes.AddItemsScreen>(
            enterTransition = {
                //Animation
                slideIntoContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }, exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }) {
            AddItemsScreen(
                onBackClickInstance = { navControllerInstance.navigateUp() },
                paddingValuesInstance = paddingValuesInstance
            )
        }

        composable<Routes.DetailsScreen>(
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }, exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }) { navBackStackEntry ->
            //we are passing the bodyPartID
            val bodyPartID = navBackStackEntry.toRoute<Routes.DetailsScreen>().bodyPartId
            DetailsScreen(
                windowSizeInstance = windowSize.widthSizeClass,
                bodyPartIDInstance = bodyPartID,
                onBackClickInstance = { navControllerInstance.navigateUp() },
                paddingValuesInstance = paddingValuesInstance
            )
        }
    }
}