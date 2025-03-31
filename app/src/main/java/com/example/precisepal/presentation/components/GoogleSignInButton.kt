package com.example.precisepal.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.precisepal.R

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    loadingState: Boolean = false,
    enableUI: Boolean = false,
    primaryText: String = "Sign in with Google",
    secondaryText: String = "Processing...",
    onButtonClick: () -> Unit,
) {
    var buttonText by remember { mutableStateOf(primaryText) }

    LaunchedEffect(key1 = loadingState) {
        buttonText = if (loadingState) secondaryText else primaryText
    }

    Button(
        onClick = { onButtonClick() }, modifier = Modifier, colors = ButtonColors(
            containerColor = Color(0xFF5863BD),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF5863BD),
            disabledContentColor = Color.White
        ), enabled = enableUI
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = "Google Logo",
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = buttonText,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )

        //conditional rendering
        //if it's in the loading state, show a circular progress indicator
        if (loadingState) {
            Spacer(modifier = Modifier.width(8.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    GoogleSignInButton(onButtonClick = {}, loadingState = false)
}