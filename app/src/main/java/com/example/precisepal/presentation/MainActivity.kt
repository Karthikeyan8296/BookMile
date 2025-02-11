package com.example.precisepal.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.precisepal.presentation.navigation.NavGraph
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
                //here we are calculating the screen size, depends up on the device
                val windowSizeClass = calculateWindowSizeClass(activity = this)
                //to remember the navigation in the app
                val navController = rememberNavController()
                //now our app will start from the navGraph
                Scaffold { paddingValues ->
                    NavGraph(
                        navControllerInstance = navController,
                        windowSize = windowSizeClass,
                        paddingValuesInstance = paddingValues
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