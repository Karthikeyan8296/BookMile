package com.example.precisepal.presentation.details

import com.example.precisepal.domain.model.BookDetails
import com.example.precisepal.domain.model.ProgressStatus
import com.example.precisepal.domain.model.TimeRange

sealed class DetailsEvent {
    data object DeleteBook: DetailsEvent()
    data object RestoreBook: DetailsEvent()
    data object AddNewPage: DetailsEvent()
    data class DeleteBookPageValue(val bookPageValue: BookDetails): DetailsEvent()
    data class ChangeProgress(val progressStatus: ProgressStatus): DetailsEvent()
    data class OnDateChange(val millis: Long?): DetailsEvent()
    data class OnTextFieldValueChange(val value: String): DetailsEvent()
    data class OnTimeRangeChange(val timeRange: TimeRange): DetailsEvent()

}