package no.schmell.backend.dtos.cms

data class QuestionTypeDto(
    val id : Int?,
    val name : String,
    val hexColor : String,
    val hint : String
)

data class UpdateQuestionTypeDto (
    val name : String?,
    val hexColor: String?,
    val hint: String?
)

data class QuestionTypeFilter (
    val nameSearch : String?,
)
