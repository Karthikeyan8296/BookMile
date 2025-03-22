package com.example.precisepal.presentation.addItems

import com.example.precisepal.domain.model.BodyPart

sealed class AddItemEvent {
    data class OnTextFieldValueChange(val value: String): AddItemEvent()
    data class OnItemClick(val bodyPart: BodyPart): AddItemEvent()
    data class onItemIsActiveChange(val bodyPart: BodyPart): AddItemEvent()
    data object onAddItemDialogDismiss: AddItemEvent()
    data object UpsertItem: AddItemEvent()
}