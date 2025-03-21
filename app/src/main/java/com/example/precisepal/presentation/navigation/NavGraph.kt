package com.example.precisepal.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.precisepal.presentation.addItems.AddItemsScreen
import com.example.precisepal.presentation.dashboard.DashboardScreen
import com.example.precisepal.presentation.dashboard.DashboardState
import com.example.precisepal.presentation.dashboard.DashboardViewModel
import com.example.precisepal.presentation.details.DetailsScreen
import com.example.precisepal.presentation.signIn.SignInScreen
import com.example.precisepal.presentation.signIn.SignInViewModel
import com.example.precisepal.presentation.util.UIEvent

@SuppressLint("NewApi")
@Composable
fun NavGraph(
    signInViewModelInstance: SignInViewModel,
    navControllerInstance: NavHostController,
    windowSize: WindowSizeClass,
    paddingValuesInstance: PaddingValues,
    snackBarHostStateInstance: SnackbarHostState,
) {
    //using outside the signInScreen, bc the signIn func takes time in completing
    LaunchedEffect(key1 = Unit) {
        signInViewModelInstance.uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> {
                    snackBarHostStateInstance.showSnackbar(event.message)
                }

                UIEvent.HideBottomSheet -> {}
            }
        }
    }
    NavHost(
        navController = navControllerInstance,
        //here we are telling, from where our app will starts
        startDestination = Routes.DashboardScreen
    )
    {

        //for which screen we will use this composable block
        composable<Routes.SignInScreen> {
            //we need to initialize the state and event here from view model
//            val signInViewModel : SignInViewModel = SignInViewModel()
//            after implementation the di
//            val signInViewModel : SignInViewModel = hiltViewModel()
            //we will get the signInViewModel from the main activity
            val state by signInViewModelInstance.state.collectAsStateWithLifecycle()
            SignInScreen(
                windowSizeInstance = windowSize.widthSizeClass,
                paddingValuesInstance = paddingValuesInstance,
                state = state,
                onEvent = signInViewModelInstance::onEvent,
                snackBarHostStateInstanceScreen = snackBarHostStateInstance,
                uiEvent = signInViewModelInstance.uiEvent
            )
        }

        composable<Routes.DashboardScreen> {
            //viewModel for dashboard
            val dashBoardViewModel: DashboardViewModel = hiltViewModel()
            val state by dashBoardViewModel.state.collectAsStateWithLifecycle()
            DashboardScreen(
                onFabClick = { navControllerInstance.navigate(Routes.AddItemsScreen) },
                //We are passing the bodyPartID
                //In that lambda function we are passing that string
                onItemCardClicked = { bodyPartID ->
                    navControllerInstance.navigate(Routes.DetailsScreen(bodyPartID))
                },
                paddingValuesInstance = paddingValuesInstance,
                snackbarHostStateInstanceScreen = snackBarHostStateInstance,
                uiEvent = dashBoardViewModel.uiEvent,
                state = state,
                onEvent = dashBoardViewModel::onEvent
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
                paddingValuesInstance = paddingValuesInstance,
                snackbarHostStateInstanceScreen = snackBarHostStateInstance
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
                paddingValuesInstance = paddingValuesInstance,
                snackbarHostStateInstanceScreen = snackBarHostStateInstance
            )
        }
    }
}