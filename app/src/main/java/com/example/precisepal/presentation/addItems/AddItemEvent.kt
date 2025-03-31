package com.example.precisepal.presentation.addItems

import com.example.precisepal.domain.model.Book

sealed class AddItemEvent {
    data class OnTextFieldValueChange(val value: String): AddItemEvent()
    data class OnItemClick(val book: Book): AddItemEvent()
    data class OnItemIsActiveChange(val book: Book): AddItemEvent()
    data object OnAddItemDialogDismiss: AddItemEvent()
    data object UpsertItem: AddItemEvent()
}