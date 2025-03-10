package com.example.precisepal.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.precisepal.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheet(
    onDismiss: () -> Unit,
    userInstance: User?,
    isOpen: Boolean,
    sheetState: SheetState,
    buttonPrimaryText: String,
    buttonLoadingState: Boolean,
    onButtonClick: () -> Unit,
) {
    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp, top = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProfilePicPlaceholder(
                    profilePicUrl = if (userInstance == null || userInstance.isAnonymous) null else userInstance.profilePic,
                    placeHolderSize = 120.dp,
                    boarderWidth = 1.dp
                )
                Text(
                    text = if (userInstance == null || userInstance.isAnonymous) "Anonymous" else userInstance.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (userInstance == null || userInstance.isAnonymous) "anonymous@measuremate.io" else userInstance.email,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(24.dp))
                GoogleSignInButton(
                    onButtonClick = { onButtonClick() },
                    loadingState = buttonLoadingState,
                    primaryText = buttonPrimaryText,
                    enableUI = true
                )
            }
        }
    }
}