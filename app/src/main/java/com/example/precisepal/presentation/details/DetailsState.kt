package com.example.precisepal.presentation.details

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.precisepal.domain.model.BodyPart
import com.example.precisepal.domain.model.BodyPartValues
import com.example.precisepal.domain.model.TimeRange
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class DetailsState(
    val bodyPart: BodyPart? = null,
    val textFieldValue: String = "",
    val recentlyDeletedBodyPart: BodyPartValues? = null,
    val date: LocalDate = LocalDate.now(),
    val timeRange: TimeRange = TimeRange.LAST_7_DAYS,
    val allBodyPartValues: List<BodyPartValues> = emptyList(),
    val chartBodyPartValues: List<BodyPartValues> = emptyList(),
)