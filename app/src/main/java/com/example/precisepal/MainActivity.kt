package com.example.precisepal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.precisepal.presentation.addItems.AddItemsScreen
import com.example.precisepal.presentation.dashboard.DashboardScreen
import com.example.precisepal.presentation.dashboard.DashboardTopBar
import com.example.precisepal.presentation.details.DetailsScreen
import com.example.precisepal.presentation.signIn.SignInScreen
import com.example.precisepal.presentation.theme.PrecisePalTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrecisePalTheme {
                //here we are calculating the screen size, depends up on the device
                val windowSizeClass = calculateWindowSizeClass(activity = this)
//                SignInScreen(windowSize = windowSizeClass.widthSizeClass)
//                DashboardScreen()
//                AddItemsScreen()
                DetailsScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PrecisePalTheme {
        //SignInScreen(windowSize = WindowWidthSizeClass.Compact)
        DashboardScreen()
    }
}