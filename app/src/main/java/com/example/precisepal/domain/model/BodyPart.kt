package com.example.precisepal.domain.model

data class BodyPart(
    val name: String,
    val isActive: Boolean,
    val progress: String,
    val currentPage: Float? = null,
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

val predefinedBodyPart: List<BodyPart> = listOf(
    BodyPart(
        name = "Atomic Habits",
        isActive = true,
        progress = ProgressStatus.COMPLETED.code,
        currentPage = 320f
    ),
    BodyPart(
        name = "Deep Work",
        isActive = true,
        progress = ProgressStatus.GOING_WELL.code,
        currentPage = 150f
    ),
    BodyPart(
        name = "The Lean Startup",
        isActive = true,
        progress = ProgressStatus.PLANNING_TO_READ.code,
        currentPage = 0f
    ),
    BodyPart(
        name = "Pragmatic Programmer",
        isActive = true,
        progress = ProgressStatus.IN_PROGRESS.code,
        currentPage = 120f
    ),
    BodyPart(
        name = "Clean Code",
        isActive = true,
        progress = ProgressStatus.IN_PROGRESS.code,
        currentPage = 250f
    ),
    BodyPart(
        name = "You Don’t Know JS",
        isActive = false,
        progress = ProgressStatus.ARCHIVED.code,
        currentPage = 80f
    ),
    BodyPart(
        name = "Eloquent JavaScript",
        isActive = true,
        progress = ProgressStatus.NOT_STARTED_YET.code,
        currentPage = 0f
    ),
    BodyPart(
        name = "The Psychology of Money",
        isActive = true,
        progress = ProgressStatus.COMPLETED.code,
        currentPage = 210f
    ),
    BodyPart(
        name = "Rich Dad Poor Dad",
        isActive = true,
        progress = ProgressStatus.GOING_WELL.code,
        currentPage = 160f
    ),
    BodyPart(
        name = "Zero to One",
        isActive = true,
        progress = ProgressStatus.PLANNING_TO_READ.code,
        currentPage = 0f
    ),
    BodyPart(
        name = "Cracking the Coding Interview",
        isActive = true,
        progress = ProgressStatus.IN_PROGRESS.code,
        currentPage = 300f
    ),
    BodyPart(
        name = "The Art of Computer Programming",
        isActive = false,
        progress = ProgressStatus.ARCHIVED.code,
        currentPage = 100f
    ),
    BodyPart(
        name = "Refactoring",
        isActive = true,
        progress = ProgressStatus.NOT_STARTED_YET.code,
        currentPage = 0f
    ),
    BodyPart(
        name = "Soft Skills: The IT Life Manual",
        isActive = true,
        progress = ProgressStatus.COMPLETED.code,
        currentPage = 270f
    )
)
