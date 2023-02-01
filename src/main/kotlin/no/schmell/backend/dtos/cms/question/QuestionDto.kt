package no.schmell.backend.dtos.cms.question

data class QuestionDto(
    val id: Int?,
    var relatedWeek: Int,
    val type: String,
    val questionDescription: String,
    val phase: Int,
    val function: QuestionFunctionDto?,
    val punishment: Int,
    val questionPicture: String?,
    val relatedGame: Int,
    val questionPictureUrl: String?
)

data class CreateQuestionDto(
    var relatedWeek: Int,
    val type: String,
    val questionDescription: String,
    val phase: Int,
    val function: CreateQuestionFunctionDto?,
    val punishment: Int,
    val relatedGame: Int
)

data class UpdateQuestionDto(
    val type: String?,
    val questionDescription: String?,
    val phase: Int?,
    val function: UpdateQuestionFunction?,
    val punishment: Int?
)