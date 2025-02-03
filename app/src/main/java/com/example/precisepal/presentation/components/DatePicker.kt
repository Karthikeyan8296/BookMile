package com.example.precisepal.presentation.components

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.DatePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePicker(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    state: DatePickerState,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel",
    onDismissReq: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (isOpen) {
        DatePickerDialog(
            modifier = modifier,
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismissReq() }) {
                    Text(text = dismissButtonText)
                }
            },
            content = {
                DatePicker(state = state)
            }
        )
    }
}