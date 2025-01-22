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
    body: @Composable () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmButtonText: String?,
    dismissButtonText: String?
) {
    if (isOpen) {
        AlertDialog(
            modifier = modifier,
            title = { Text(text = title) },
            text = body,
            onDismissRequest = { onDismiss() },
            confirmButton = {
                if(confirmButtonText != null){
                    TextButton(onClick = { onConfirm() }) {
                        Text(text = confirmButtonText)
                    }
                }
            },
            dismissButton = {
                if(dismissButtonText != null){
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = dismissButtonText)
                    }
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
        body = { Text("Yah my body") },
        confirmButtonText = "yeah",
        dismissButtonText = "nah"
    )

}