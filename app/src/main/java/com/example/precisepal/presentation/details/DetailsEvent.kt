package com.example.precisepal.presentation.details

import com.example.precisepal.domain.model.BodyPartValues
import com.example.precisepal.domain.model.MeasuringUnit
import com.example.precisepal.domain.model.TimeRange

sealed class DetailsEvent {
    data object DeleteBodyPart: DetailsEvent()
    data object RestoreBodyPart: DetailsEvent()
    data object AddNewValue: DetailsEvent()
    data class DeleteBodyPartValue(val bodyPartValue: BodyPartValues): DetailsEvent()
    data class ChangeMeasuringUnit(val measuringUnit: MeasuringUnit): DetailsEvent()
    data class OnDateChange(val millis: Long?): DetailsEvent()
    data class OnTextFieldValueChange(val value: String): DetailsEvent()
    data class OnTimeRangeChange(val timeRange: TimeRange): DetailsEvent()

}