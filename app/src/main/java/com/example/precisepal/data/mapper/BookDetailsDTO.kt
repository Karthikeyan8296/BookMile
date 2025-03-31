package com.example.precisepal.data.mapper

import android.annotation.SuppressLint
import com.example.precisepal.domain.model.BookDetails
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class BookDetailsDTO(
    val value: Float = 0f,
    val date: Timestamp = Timestamp.now(),
    val bookId: String? = null,
    val bookPagesID: String? = null,
)

fun BookDetailsDTO.toBodyPartValues(): BookDetails {
    return BookDetails(
        value = value,
        date = date.toLocalDate(),
        bookId = bookId,
        bookPagesID = bookPagesID,
    )
}

fun BookDetails.toBodyPartValueDTO(): BookDetailsDTO {
    return BookDetailsDTO(
        value = value,
        date = date.toTimestamp(),
        bookId = bookId,
        bookPagesID = bookPagesID,
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