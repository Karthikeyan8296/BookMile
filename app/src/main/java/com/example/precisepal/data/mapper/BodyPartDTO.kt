package com.example.precisepal.data.mapper

import com.example.precisepal.domain.model.BodyPart

data class BodyPartDTO(
    val name: String = "",
    val active: Boolean = false,
    val measuringUnit: String = "",
    val latestValue: Float? = null,
    val bodyPartId: String? = null,
)

fun BodyPart.toBodyPartDTO() : BodyPartDTO{
    return BodyPartDTO(
        name = name,
        active = isActive,
        measuringUnit = progress,
        latestValue = currentPage,
        bodyPartId = bookId,
    )
}

fun BodyPartDTO.toBodyPart() : BodyPart{
    return BodyPart(
        name = name,
        isActive = active,
        progress = measuringUnit,
        currentPage = latestValue,
        bookId = bodyPartId,
    )
}