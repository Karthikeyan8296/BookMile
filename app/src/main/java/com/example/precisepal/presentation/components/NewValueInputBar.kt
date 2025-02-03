package com.example.precisepal.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NewValueInputBar(
    modifier: Modifier,
    value: String,
    date: String,
    onValueChange: (String) -> Unit,
    onDoneClick: () -> Unit,
    onCalenderIconClick: () -> Unit,
    onDoneImeActionClick: () -> Unit,
    isInputValueCardVisible: Boolean,
) {

    var inputError by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    inputError = when {
        value.isBlank() -> "Please enter your gains"
        value.toFloatOrNull() == null -> "Invalid value"
        value.toFloat() < 0f -> "Value cannot be negative"
        value.toFloat() > 1000 -> "Please set maximum of 1000"
        else -> null
    }

    //this will animate the visibility of the card
    AnimatedVisibility(
        modifier = modifier,
        visible = isInputValueCardVisible,
        enter = slideInVertically(tween(durationMillis = 300)) { h -> h },
        exit = slideOutVertically(tween(durationMillis = 300)) { h -> h }
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = "log your gains") },
                value = value,
                onValueChange = onValueChange,
                //this will not go to next line
                singleLine = true,
                //error message will show if it has error
                isError = inputError != null && value.isNotBlank(),
                supportingText = { Text(text = inputError.orEmpty()) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                //in keyboard if we press done, it will close the keyboard
                keyboardActions = KeyboardActions(onDone = { onDoneImeActionClick() }),
                trailingIcon = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = date)
                        IconButton(
                            onClick = { onCalenderIconClick() }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DateRange,
                                contentDescription = "done"
                            )
                        }
                    }
                }
            )
            FilledIconButton(
                onClick = { onDoneClick() },
                modifier = Modifier
                    .size(46.dp),
                //if it has error, then it will not enable the button
                enabled = inputError == null
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Send,
                    contentDescription = "done"
                )
            }
        }
    }
}


@Preview(showBackground = false)
@Composable
private fun NewValueInputBarPreview() {
    NewValueInputBar(
        value = "type your weight",
        date = "12 June 2025",
        onValueChange = {},
        onDoneClick = {},
        modifier = Modifier,
        onDoneImeActionClick = {},
        onCalenderIconClick = {},
        isInputValueCardVisible = true
    )
}