package com.example.precisepal.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.precisepal.R

@Composable
fun AnonymouslySignInButton(
    modifier: Modifier,
    loadingState: Boolean = false,
    enableUI: Boolean = false,
    primaryText: String = "Continue without Login",
    secondaryText: String = "Processing...",
    onButtonClick: () -> Unit,
) {
    var buttonText by remember { mutableStateOf(primaryText) }

    //LaunchedEffect is used to update the button text based on the loading state
    //its is like useEffect - sideEffect
    LaunchedEffect(key1 = loadingState) {
        buttonText = if (loadingState) secondaryText else primaryText
    }

    TextButton(onClick = { onButtonClick() }, modifier = Modifier, enabled = enableUI) {
        Text(
            text = buttonText,
            color = MaterialTheme.colorScheme.inversePrimary,
            fontWeight = FontWeight.SemiBold
        )

        //conditional rendering
        //if it's in the loading state, show a circular progress indicator
        if (loadingState) {
            Spacer(modifier = Modifier.width(8.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AnonymouslySignInButton(onButtonClick = {}, modifier = Modifier, loadingState = false)
}