package com.example.precisepal.presentation.addItems

import com.example.precisepal.domain.model.BodyPart

data class AddItemsState(
    val textFieldValue: String = "",
    val selectedBodyPart: String = "",
    val bodyParts: List<BodyPart> = emptyList()
)
