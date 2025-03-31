package com.example.precisepal.presentation.Loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.precisepal.presentation.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    paddingValuesInstance: PaddingValues,
    navControllerInstance: NavHostController,
) {
    LaunchedEffect(Unit) {
        delay(2000) // 2-second delay
        navControllerInstance.navigate(Routes.DashboardScreen) {
            popUpTo(Routes.LoadingScreen) {
                inclusive = true
            } // Remove LoadingScreen from back stack
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(paddingValuesInstance),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp,
            color = Color(0xFF5863BD)
        )
    }
}