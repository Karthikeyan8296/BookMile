package com.example.precisepal.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.precisepal.domain.model.BodyPart
import com.example.precisepal.domain.model.User
import com.example.precisepal.domain.model.predefinedBodyPart
import com.example.precisepal.presentation.components.MeasureMateDialog
import com.example.precisepal.presentation.components.ProfileBottomSheet
import com.example.precisepal.presentation.components.ProfilePicPlaceholder
import com.example.precisepal.presentation.theme.PrecisePalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    //Dummy data!
    val user = User(
        name = "Karthik",
        email = "Karthik@measuremate.io",
        userID = "abx07",
        profilePic = "https://images.pexels.com/photos/14653174/pexels-photo-14653174.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
        isAnonymous = false,
    )

    //Sign out button state
    var isDialogSignOut by rememberSaveable {
        mutableStateOf(false)

    }
    MeasureMateDialog(
        body = "are you sure want to sign out?",
        title = "Sign Out",
        isOpen = isDialogSignOut,
        onDismiss = {isDialogSignOut = false},
        onConfirm = {isDialogSignOut = false},
        confirmButtonText = "Yes",
        dismissButtonText = "No"
    )
    //Button sheet state
    var isProfileSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    ProfileBottomSheet(
        onDismiss = {isProfileSheetOpen = false}, isOpen = isProfileSheetOpen,
        sheetState = rememberModalBottomSheetState(),
        buttonPrimaryText = "Sign out from Google",
        buttonLoadingState = false,
        onButtonClick = {isDialogSignOut = true},
        userInstance = user
    )

    //main content
    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            //Dashboard header
            DashboardTopBar(
                onProfileClicks = { isProfileSheetOpen = true},
                profilePicURL = "https://images.pexels.com/photos/14653174/pexels-photo-14653174.jpeg"
            )

            //responsive lazy layout
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 300.dp)
            ) {
                //if we are implementing single card at top, we can use item!
                //item {}
                //bunch of cards can be defined here, with items!
                items(predefinedBodyPart) { bodyPart ->
                    ItemCard(bodyPartInstance = bodyPart)
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onClick = {}) {
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
        modifier = Modifier.padding(horizontal = 8.dp),
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

//Body component//
@Composable
//the bodyPart is called from the domain
private fun ItemCard(bodyPartInstance: BodyPart) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        onClick = { },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    "${bodyPartInstance.latestValue ?: ""} ${bodyPartInstance.measuringUnit}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(32.dp)
                        .background(color = MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = "null"
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
        DashboardScreen()
    }
}