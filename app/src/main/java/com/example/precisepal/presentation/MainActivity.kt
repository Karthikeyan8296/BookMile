package com.example.precisepal.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.precisepal.domain.model.AuthStatus
import com.example.precisepal.presentation.navigation.NavGraph
import com.example.precisepal.presentation.navigation.Routes
import com.example.precisepal.presentation.signIn.SignInViewModel
import com.example.precisepal.presentation.theme.PrecisePalTheme
import dagger.hilt.android.AndroidEntryPoint

//Android entry point is used to inject the dependencies in the activity
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            PrecisePalTheme {
                //here we are calculating the screen size, depends up on the device//
                val windowSizeClass = calculateWindowSizeClass(activity = this)
                //to remember the navigation in the app//
                val navController = rememberNavController()
                val snackBarHostState = remember { SnackbarHostState() }
                //auth status//
                val signInViewModel: SignInViewModel = hiltViewModel()
                val authStatus by signInViewModel.authStatus.collectAsStateWithLifecycle()
                //to remember the preview screen, so while rotating it will be in the same screen
                var previousAuthStatus by rememberSaveable { mutableStateOf<AuthStatus?>(null) }
                LaunchedEffect(key1 = authStatus) {
                    if (previousAuthStatus != authStatus) {
                        when (authStatus) {
                            AuthStatus.LOADING -> {}
                            AuthStatus.AUTHENTICATED -> navController.navigate(Routes.DashboardScreen) {
                                //we will not navigate back
                                popUpTo(
                                    0
                                )
                            }
                            AuthStatus.UNAUTHENTICATED -> navController.navigate(Routes.SignInScreen) {
                                popUpTo(
                                    0
                                )
                            }
                        }
                    }
                    previousAuthStatus = authStatus
                }
                //now our app will start from the navGraph//
                Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { paddingValues ->
                    NavGraph(
                        signInViewModelInstance = signInViewModel,
                        navControllerInstance = navController,
                        windowSize = windowSizeClass,
                        paddingValuesInstance = paddingValues,
                        snackBarHostStateInstance = snackBarHostState
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PrecisePalTheme {}
}