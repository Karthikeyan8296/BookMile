package com.example.precisepal.domain.model

import java.time.LocalDate

//data class is created because we will be getting the data from the backend firebase
data class BookDetails(
    val value :Float,
    val date: LocalDate,
    val bookId: String? = null,
    val bookPagesID: String? = null
)