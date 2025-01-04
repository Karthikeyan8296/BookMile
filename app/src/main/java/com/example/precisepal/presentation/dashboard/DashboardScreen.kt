package com.example.precisepal.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.precisepal.presentation.components.ProfilePicPlaceholder

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        DashboardTopBar(
            onProfileClicks = {},
            profilePicURL = "https://images.pexels.com/photos/14653174/pexels-photo-14653174.jpeg"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(
    profilePicURL: String?,
    onProfileClicks: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = "PrecisePal",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        },
        actions = {
            IconButton(onClick = { onProfileClicks() }) {
                ProfilePicPlaceholder(
                    placeHolderSize = 40.dp,
                    boarderWidth = 1.dp,
                    profilePicUrl = profilePicURL
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}