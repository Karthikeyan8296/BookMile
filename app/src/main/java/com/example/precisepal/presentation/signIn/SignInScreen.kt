package com.example.precisepal.presentation.signIn

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCbrt
import com.example.precisepal.R
import com.example.precisepal.presentation.components.AnonymouslySignInButton
import com.example.precisepal.presentation.components.GoogleSignInButton
import com.example.precisepal.presentation.components.MeasureMateDialog


@Composable
fun SignInScreen(windowSizeInstance: WindowWidthSizeClass) {

    //Dialog
    var isDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    MeasureMateDialog(
        isOpen = isDialogOpen,
        onDismiss = { isDialogOpen = false },
        onConfirm = {isDialogOpen = false},
        title = "welcome back boobs",
        body = { Text("yah my body") },
        confirmButtonText = "Yes",
        dismissButtonText = "Cancel"
    )

    when (windowSizeInstance) {
        //Compact is the normal mobile screen view, so if normal screen view na.. use this UI
        WindowWidthSizeClass.Compact -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splash_screen_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(160.dp)
                )
                Spacer(modifier = Modifier.height(220.dp))

                Text(
                    text = "PrecisePal",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,

                    )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.onboarding_into),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                GoogleSignInButton(loadingState = false, onButtonClick = {})
                AnonymouslySignInButton(
                    loadingState = false,
                    modifier = Modifier,
                    onButtonClick = {isDialogOpen = true})
            }
        }
        //Else use this UI
        else -> {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splash_screen_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(160.dp)
                    )
                    Text(
                        text = "PrecisePal",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        )
                    Text(
                        text = stringResource(R.string.onboarding_into),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 20.dp)

                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    GoogleSignInButton(
                        loadingState = false,
                        onButtonClick = {})
                    AnonymouslySignInButton(
                        loadingState = false,
                        modifier = Modifier,
                        onButtonClick = {isDialogOpen = true})
                }
            }
        }
    }


}

@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
private fun Preview() {
    SignInScreen(
        //Medium is used to check the screen in landscape mode
        //Compact is used to check the screen in portrait mode
        windowSizeInstance = WindowWidthSizeClass.Compact
    )
}