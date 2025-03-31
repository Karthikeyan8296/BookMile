package com.example.precisepal.presentation.dashboard

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.precisepal.R
import com.example.precisepal.domain.model.BodyPart
import com.example.precisepal.domain.model.predefinedBodyPart
import com.example.precisepal.presentation.components.BookMileDialog
import com.example.precisepal.presentation.components.ProfileBottomSheet
import com.example.precisepal.presentation.components.ProfilePicPlaceholder
import com.example.precisepal.presentation.theme.PrecisePalTheme
import com.example.precisepal.presentation.util.UIEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onFabClick: () -> Unit,
    //we are passing string for that body part id
    onItemCardClicked: (String) -> Unit,
    paddingValuesInstance: PaddingValues,
    uiEvent: Flow<UIEvent>,
    onEvent: (DashboardEvents) -> Unit,
    state: DashboardState,
    snackbarHostStateInstanceScreen: SnackbarHostState,
) {
    //Dummy data!
//    val user = User(
//        name = "Karthik",
//        email = "Karthik@measuremate.io",
//        userID = "abx07",
//        profilePic = "https://images.pexels.com/photos/14653174/pexels-photo-14653174.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
//        isAnonymous = false,
//    )

    val context = LocalContext.current

    //Button sheet state
    var isProfileSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val preBuildSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    //snack bar
    LaunchedEffect(key1 = Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> {
                    snackbarHostStateInstanceScreen.showSnackbar(event.message)
                }

                UIEvent.HideBottomSheet -> {
                    scope.launch { preBuildSheetState.hide() }.invokeOnCompletion {
                        if (!preBuildSheetState.isVisible) {
                            isProfileSheetOpen = false
                        }
                    }
                }

                UIEvent.NavigateBack -> TODO()
            }
        }
    }

    //Sign out button state
    var isDialogSignOut by rememberSaveable {
        mutableStateOf(false)

    }
    BookMileDialog(
        body = { Text("Are you sure you want to sign out? You can sign back in anytime.") },
        title = "Sign out",
        isOpen = isDialogSignOut,
        onDismiss = { isDialogSignOut = false },
        onConfirm = {
            onEvent(DashboardEvents.SignOut)
            isDialogSignOut = false
        },
        confirmButtonText = "Confirm",
        dismissButtonText = "Cancel"
    )

    //bottom sheet
    val isUserAnonymous = state.user?.isAnonymous ?: true

    ProfileBottomSheet(
        onDismiss = { isProfileSheetOpen = false },
        isOpen = isProfileSheetOpen,
        sheetState = preBuildSheetState,
        buttonPrimaryText = if (isUserAnonymous) "Sign in with Google" else "Sign out from Google",
        buttonLoadingState = if (isUserAnonymous) state.isSignInButtonLoading else state.isSignOutButtonLoading,
        onButtonClick = {
            if (isUserAnonymous) {
//                onEvent(DashboardEvents.AnonymousUserSignInWithGoogle(context))
                onEvent(DashboardEvents.SignOut)
            } else {
                isDialogSignOut = true
            }
        },
        userInstance = state.user
    )


    //Main Screen UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValuesInstance)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF6F6F6)),
        ) {
            //Dashboard header
            DashboardTopBar(
                onProfileClicks = { isProfileSheetOpen = true },
                profilePicURL = state.user?.profilePic
            )

            //responsive lazy layout
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 300.dp)
            ) {
                //if we are implementing single card at top, we can use item!
                //item {}
                //bunch of cards can be defined here, with items!
                items(state.bodyParts) { bodyPart ->
                    ItemCard(bodyPartInstance = bodyPart, onCardClick = onItemCardClicked)
                }
            }
        }
        LaunchedEffect(state.bodyParts) {
            Log.d("lazy", "Google signIn error ${state.bodyParts}")
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onClick = { onFabClick() }) {
            Icon(imageVector = Icons.Rounded.AddCircle, contentDescription = "add icon")
        }
    }
}

//Header component//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(
    profilePicURL: String?,
    onProfileClicks: () -> Unit,
) {
    TopAppBar(
        //removing the default padding in the topBar
        windowInsets = WindowInsets(0, 0, 0, 0),
        modifier = Modifier.padding(horizontal = 8.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF6F6F6)),
        title = {
            Text(
                text = "BookMile",
                color = Color(0xFF5863BD),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        },
        actions = {
            IconButton(onClick = { onProfileClicks() }) {
                ProfilePicPlaceholder(
                    placeHolderSize = 40.dp,
                    boarderWidth = 0.6.dp,
                    profilePicUrl = profilePicURL
                )
            }
        },
    )
}

//Body component//
@Composable
//the bodyPart is called from the domain
private fun ItemCard(bodyPartInstance: BodyPart, onCardClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.White
        ),
        //if the body part is not null, then we will call this click fun
        onClick = { bodyPartInstance.bookId?.let { onCardClick(it) } },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
                    .background(color = Color(0xFFE8ECFF)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.book),
                    contentDescription = "null",
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = bodyPartInstance.name,
                //to make sure if the text is too long it doesn't crash the app
                modifier = Modifier.weight(8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "${bodyPartInstance.currentPage ?: ""} ${bodyPartInstance.progress}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(32.dp)
                        .background(color = Color(0xFFF6F6F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = "null",
                    )
                }
            }
        }
    }
}

//@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    PrecisePalTheme {
        DashboardScreen(
            onItemCardClicked = {},
            onFabClick = {},
            paddingValuesInstance = PaddingValues(0.dp),
            snackbarHostStateInstanceScreen = SnackbarHostState(),
            uiEvent = flowOf(),
            onEvent = {},
            state = DashboardState(bodyParts = predefinedBodyPart)
        )
    }
}