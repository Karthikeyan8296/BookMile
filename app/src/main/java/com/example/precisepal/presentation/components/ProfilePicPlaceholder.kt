package com.example.precisepal.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.precisepal.R

@Composable
fun ProfilePicPlaceholder(
    placeHolderSize: Dp,
    boarderWidth: Dp,
    profilePicUrl: String?,
) {
    val imageReq =
        ImageRequest.Builder(LocalContext.current).data(profilePicUrl).crossfade(true).build()
    Box(
        modifier = Modifier
            .size(placeHolderSize)
            .border(
                width = boarderWidth, color = Color.Gray,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageReq,
            contentDescription = "Profile Pic",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.splash_screen_logo),
            error = painterResource(R.drawable.splash_screen_logo),
            //if suppose profile pic rectangle shape la erutha! change to circle shape
            modifier = Modifier.clip(CircleShape).fillMaxSize()
        )
    }
}