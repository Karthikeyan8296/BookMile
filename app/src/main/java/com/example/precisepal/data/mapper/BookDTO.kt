package com.example.precisepal.data.mapper

import com.example.precisepal.domain.model.Book

data class BookDTO(
    val name: String = "",
    val active: Boolean = false,
    val progress: String = "",
    val latestValue: Float? = null,
    val bodyPartId: String? = null,
)

fun Book.toBookDTO(): BookDTO {
    return BookDTO(
        name = name,
        active = isActive,
        progress = progress,
        latestValue = currentPage,
        bodyPartId = bookId,
    )
}

fun BookDTO.toBook(): Book {
    return Book(
        name = name,
        isActive = active,
        progress = progress,
        currentPage = latestValue,
        bookId = bodyPartId,
    )
}