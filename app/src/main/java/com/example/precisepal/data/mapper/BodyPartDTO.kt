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
        measuringUnit = measuringUnit,
        latestValue = latestValue,
        bodyPartId = bodyPartId,
    )
}

fun BodyPartDTO.toBodyPart() : BodyPart{
    return BodyPart(
        name = name,
        isActive = active,
        measuringUnit = measuringUnit,
        latestValue = latestValue,
        bodyPartId = bodyPartId,
    )
}