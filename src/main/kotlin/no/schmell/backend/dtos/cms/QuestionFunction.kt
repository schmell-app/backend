package no.schmell.backend.dtos.cms

data class QuestionFunctionDto(
    val id : Int?,
    val timer : Int?,
    val answer : String?,
    val challenges : List<String>?,
    val questions : List<String>?,
    val options : List<String>?
)

data class CreateQuestionFunctionDto (
    val timer : Int?,
    val answer : String?,
    val challenges : List<String>?,
    val questions : List<String>?,
    val options : List<String>?
)

data class UpdateQuestionFunction (
    val id: Int?,
    val timer : Int?,
    val answer : String?,
    val challenges : List<String>?,
    val questions : List<String>?,
    val options : List<String>?
)