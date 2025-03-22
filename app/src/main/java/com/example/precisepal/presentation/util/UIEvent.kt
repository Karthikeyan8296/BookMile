package com.example.precisepal.presentation.util

sealed class UIEvent {
    data class ShowSnackBar(val message: String) : UIEvent()
    data object HideBottomSheet: UIEvent()
    data object NavigateBack: UIEvent()
}