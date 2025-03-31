package com.example.precisepal.presentation.addItems

import com.example.precisepal.domain.model.Book

data class AddItemsState(
    val textFieldValue: String = "",
    val selectedBook: Book? = null,
    val books: List<Book> = emptyList(),
)
