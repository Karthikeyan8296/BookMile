package com.example.precisepal.presentation.util

sealed class UIEvent {
    data class ShowSnackBar(
        val message: String,
        val actionLabel: String? = null,
    ) : UIEvent()

    data object HideBottomSheet : UIEvent()
    data object NavigateBack : UIEvent()
}