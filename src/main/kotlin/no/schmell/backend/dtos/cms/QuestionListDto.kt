package no.schmell.backend.dtos.cms

data class QuestionListDto(
    val id: Int?,
    var relatedWeek: Int?,
    val type: String,
    val questionDescription: String,
    val phase: Int,
    val function: String?,
    val punishment: Int,
    val questionPicture: String?,
    val relatedGame: Int?,
    val questionPictureUrl: String?
)