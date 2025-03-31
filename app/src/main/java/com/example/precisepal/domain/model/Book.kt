package com.example.precisepal.domain.model

data class Book(
    val name: String,
    val isActive: Boolean,
    val progress: String,
    val currentPage: Float? = 0f,
    val bookId: String? = null,
)

enum class ProgressStatus(
    val code: String,
    val label: String,
) {
    COMPLETED("✅", "Completed"),
    GOING_WELL("👍", "Going Well"),
    PLANNING_TO_READ("🔍", "Planning to Read"),
    ARCHIVED("📁", "Archived"),
    IN_PROGRESS("⏳", "In Progress"),
    NOT_STARTED_YET("❌", "Not Started Yet")
}

val predefinedBooks: List<Book> = listOf(
    Book(
        name = "Atomic Habits",
        isActive = true,
        progress = ProgressStatus.COMPLETED.code,
        currentPage = 320f
    ),
    Book(
        name = "Deep Work",
        isActive = true,
        progress = ProgressStatus.GOING_WELL.code,
        currentPage = 150f
    ),
    Book(
        name = "The Lean Startup",
        isActive = true,
        progress = ProgressStatus.PLANNING_TO_READ.code,
        currentPage = 0f
    ),
    Book(
        name = "Pragmatic Programmer",
        isActive = true,
        progress = ProgressStatus.IN_PROGRESS.code,
        currentPage = 120f
    ),
    Book(
        name = "Clean Code",
        isActive = true,
        progress = ProgressStatus.IN_PROGRESS.code,
        currentPage = 250f
    ),
    Book(
        name = "You Don’t Know JS",
        isActive = false,
        progress = ProgressStatus.ARCHIVED.code,
        currentPage = 80f
    ),
    Book(
        name = "Eloquent JavaScript",
        isActive = true,
        progress = ProgressStatus.NOT_STARTED_YET.code,
        currentPage = 0f
    ),
    Book(
        name = "The Psychology of Money",
        isActive = true,
        progress = ProgressStatus.COMPLETED.code,
        currentPage = 210f
    ),
    Book(
        name = "Rich Dad Poor Dad",
        isActive = true,
        progress = ProgressStatus.GOING_WELL.code,
        currentPage = 160f
    ),
    Book(
        name = "Zero to One",
        isActive = true,
        progress = ProgressStatus.PLANNING_TO_READ.code,
        currentPage = 0f
    ),
    Book(
        name = "Cracking the Coding Interview",
        isActive = true,
        progress = ProgressStatus.IN_PROGRESS.code,
        currentPage = 300f
    ),
    Book(
        name = "The Art of Computer Programming",
        isActive = false,
        progress = ProgressStatus.ARCHIVED.code,
        currentPage = 100f
    ),
    Book(
        name = "Refactoring",
        isActive = true,
        progress = ProgressStatus.NOT_STARTED_YET.code,
        currentPage = 0f
    ),
    Book(
        name = "Soft Skills: The IT Life Manual",
        isActive = true,
        progress = ProgressStatus.COMPLETED.code,
        currentPage = 270f
    )
)
