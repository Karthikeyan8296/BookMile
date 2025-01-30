package com.example.precisepal.domain.model

import java.time.LocalDate

//data class is created because we will be getting the data from the backend firebase
data class BodyPartValues(
    val value :Float,
    val date: LocalDate,
    val bodyPartId: String? = null,
    val bodyPartValueID: String? = null
)