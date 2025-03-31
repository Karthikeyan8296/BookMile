package com.example.precisepal.presentation.details

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.precisepal.domain.model.Book
import com.example.precisepal.domain.model.BookDetails
import com.example.precisepal.domain.model.TimeRange
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class DetailsState(
    val bookName: Book? = null,
    val textFieldValue: String = "",
    val recentlyDeletedBook: BookDetails? = null,
    val date: LocalDate = LocalDate.now(),
    val timeRange: TimeRange = TimeRange.ALL_TIME,
    val allBookPageValues: List<BookDetails> = emptyList(),
    val graphBookPageValues: List<BookDetails> = emptyList(),
)