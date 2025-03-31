package com.example.precisepal.presentation.signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.precisepal.R
import com.example.precisepal.presentation.components.AnonymouslySignInButton
import com.example.precisepal.presentation.components.GoogleSignInButton
import com.example.precisepal.presentation.components.BookMileDialog
import com.example.precisepal.presentation.theme.InterFontFamily
import com.example.precisepal.presentation.util.UIEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun SignInScreen(
    paddingValuesInstance: PaddingValues,
    windowSizeInstance: WindowWidthSizeClass,
    state: SignInState,
    onEvent: (SignInEvent) -> Unit,
    uiEvent: Flow<UIEvent>,
    snackBarHostStateInstanceScreen: SnackbarHostState,
) {
    val contextInstance = LocalContext.current

    //snack bar -> it is in the navGraph
//    LaunchedEffect(key1 = Unit) {
//        uiEvent.collect { event ->
//            when (event) {
//                is UIEvent.ShowSnackBar -> {
//                    snackBarHostStateInstanceScreen.showSnackbar(event.message)
//                }
//            }
//        }
//    }

    //Dialog
    var isDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    BookMileDialog(
        isOpen = isDialogOpen,
        onDismiss = { isDialogOpen = false },
        //connecting the events
        onConfirm = {
            onEvent(SignInEvent.SignInAnonymously)
            isDialogOpen = false
        },
        title = "Continue as Guest",
        body = { Text("You can continue as a guest, but your data will be lost when you sign out.") },
        confirmButtonText = "Continue",
        dismissButtonText = "Cancel"
    )

    when (windowSizeInstance) {
        //Compact is the normal mobile screen view, so if normal screen view na.. use this UI
        WindowWidthSizeClass.Compact -> {
            Column(
                modifier = Modifier
                    .padding(paddingValuesInstance)
                    .fillMaxSize()
                    .background(color = Color(0xFFF6F6F6)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.full_sized_logo),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "BookMile",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        color = Color(0xFF5863BD),
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.intro),
                    contentDescription = "Logo",
                    modifier = Modifier.size(540.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column {
                            Text(
                                text = "Read, Track, Achieve.",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5863BD),
                                fontSize = 28.sp,
                                letterSpacing = 0.5.sp,
                                fontFamily = InterFontFamily,
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = "Upgrade Your Reading Journey with BookMile!",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontFamily = InterFontFamily,
                                letterSpacing = 0.2.sp,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Track your books, set goals, and stay motivated as you read. Start now and make every page count!",
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray,
                                letterSpacing = 0.2.sp,
                                lineHeight = 20.sp,
                                fontSize = 14.sp,
                                fontFamily = InterFontFamily,
                                textAlign = TextAlign.Start
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        GoogleSignInButton(
                            //state
                            loadingState = state.isGoogleSignInButtonLoading,
                            enableUI = !state.isGoogleSignInButtonLoading && !state.isAnonymousSignInButtonLoading,
                            //events
                            onButtonClick = { onEvent(SignInEvent.SignInWithGoogle(context = contextInstance)) })
                        Spacer(modifier = Modifier.height((-10).dp))
                        AnonymouslySignInButton(
                            //state
                            loadingState = state.isAnonymousSignInButtonLoading,
                            enableUI = !state.isAnonymousSignInButtonLoading && !state.isGoogleSignInButtonLoading,
                            modifier = Modifier,
                            onButtonClick = { isDialogOpen = true }
                        )
                    }

                }
            }
        }
        //Else use this UI
        else -> {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFF6F6F6)),
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
                        painter = painterResource(id = R.drawable.intro),
                        contentDescription = "Logo",
                        modifier = Modifier.size(360.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(color = Color.White)
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column {
                        Text(
                            text = "Read, Track, Achieve.",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5863BD),
                            fontSize = 28.sp,
                            letterSpacing = 0.5.sp,
                            fontFamily = InterFontFamily,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = "Upgrade Your Reading Journey with BookMile!",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontFamily = InterFontFamily,
                            letterSpacing = 0.2.sp,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Track your books, set goals, and stay motivated as you read. Start now and make every page count!",
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray,
                            letterSpacing = 0.2.sp,
                            lineHeight = 20.sp,
                            fontSize = 14.sp,
                            fontFamily = InterFontFamily,
                            textAlign = TextAlign.Start
                        )
                    }
                    Spacer(modifier = Modifier.height(46.dp))

                    GoogleSignInButton(
                        //state
                        loadingState = state.isGoogleSignInButtonLoading,
                        enableUI = !state.isGoogleSignInButtonLoading && !state.isAnonymousSignInButtonLoading,
                        //events
                        onButtonClick = { onEvent(SignInEvent.SignInWithGoogle(context = contextInstance)) })
                    AnonymouslySignInButton(
                        //state
                        loadingState = state.isAnonymousSignInButtonLoading,
                        enableUI = !state.isAnonymousSignInButtonLoading && !state.isGoogleSignInButtonLoading,
                        modifier = Modifier,
                        onButtonClick = { isDialogOpen = true })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFunction() {
    SignInScreen(
        //Medium is used to check the screen in landscape mode
        //Compact is used to check the screen in portrait mode
        windowSizeInstance = WindowWidthSizeClass.Compact,
        paddingValuesInstance = PaddingValues(0.dp),
        state = SignInState(),
        onEvent = {},
        snackBarHostStateInstanceScreen = SnackbarHostState(),
        uiEvent = flowOf()
    )
}