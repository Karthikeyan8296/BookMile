package com.example.precisepal.data.mapper

import android.annotation.SuppressLint
import com.example.precisepal.domain.model.BodyPartValues
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class BodyPartValueDTO(
    val value: Float = 0.0f,
    val date: Timestamp = Timestamp.now(),
    val bodyPartId: String? = null,
    val bodyPartValueID: String? = null,
)

fun BodyPartValueDTO.toBodyPartValues(): BodyPartValues {
    return BodyPartValues(
        value = value,
        date = date.toLocalDate(),
        bodyPartId = bodyPartId,
        bodyPartValueID = bodyPartValueID,
    )
}

fun BodyPartValues.toBodyPartValueDTO(): BodyPartValueDTO {
    return BodyPartValueDTO(
        value = value,
        date = date.toTimestamp(),
        bodyPartId = bodyPartId,
        bodyPartValueID = bodyPartValueID,
    )
}

@SuppressLint("NewApi")
private fun Timestamp.toLocalDate(): LocalDate {
    return Instant
        .ofEpochSecond(seconds, nanoseconds.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

@SuppressLint("NewApi")
private fun LocalDate.toTimestamp(): Timestamp {
    val instant: Instant = this
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
    return Timestamp(instant.toEpochMilli() / 1000, instant.nano % 1000000)
}