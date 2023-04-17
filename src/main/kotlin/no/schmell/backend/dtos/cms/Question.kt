package no.schmell.backend.dtos.cms

data class QuestionDto(
    val id: Int?,
    val activeWeeks: List<Int>,
    val type: String,
    val questionDescription: String,
    val phase: Int,
    val function: QuestionFunctionDto?,
    val punishment: Int?,
    val questionPicture: String?,
    val relatedGame: Int,
    val questionPictureUrl: String?,
    val questionType: QuestionTypeDto
)

data class CreateQuestionDto(
    var activeWeeks: List<Int>,
    val type: String,
    val questionDescription: String,
    val phase: Int,
    val function: CreateQuestionFunctionDto?,
    val punishment: Int?,
    val relatedGame: Int,
    val relatedQuestionType: Int,
)

data class UpdateQuestionDto(
    val type: String?,
    val questionDescription: String?,
    val phase: Int?,
    val function: UpdateQuestionFunction?,
    val punishment: Int?,
    val activeWeeks: List<Int>?,
    val relatedQuestionType: Int
)

data class GamePlayResponse(
    val uneditedQuestions: List<QuestionDto>,
    val editedQuestions: List<QuestionDto>
)
data class GamePlayParams(
    val weekNumber: Int,
    val players: List<String>
)

data class QuestionFilter(
    val weekNumber: Int?,
    val sort: String?,
    val apiFunction: String?,
)

data class AddPlayersInGameParams(
    val players: List<String>,
    val currentIndex: Int,
    val uneditedQuestions: List<QuestionDto>,
    val editedQuestions: List<QuestionDto>,
)