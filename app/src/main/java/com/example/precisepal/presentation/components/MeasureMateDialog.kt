package com.example.precisepal.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MeasureMateDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    title: String,
    body: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmButtonText: String = "Yes",
    dismissButtonText: String = "Cancel",
) {
    if (isOpen) {
        AlertDialog(
            modifier = modifier,
            title = { Text(text = title) },
            text = { Text(text = body) },
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = dismissButtonText)
                }
            },
        )
    }

}

@Preview
@Composable
private fun MatesDialogPreview() {
    MeasureMateDialog(
        isOpen = true,
        onDismiss = {},
        onConfirm = {},
        title = "welcome back boobs",
        body = "yah my body"
    )

}