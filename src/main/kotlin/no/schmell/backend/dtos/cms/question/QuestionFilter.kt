package no.schmell.backend.dtos.cms.question

data class QuestionFilter(
    val relatedWeek: Int?,
    val sort: String?,
    val apiFunction: String?,
)