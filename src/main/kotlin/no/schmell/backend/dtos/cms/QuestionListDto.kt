package no.schmell.backend.dtos.cms

import java.net.URL

data class QuestionListDto(
    val id: Int?,
    var relatedWeek: Int?,
    val type: String,
    val questionDescription: String,
    val phase: Int,
    val function: String?,
    val punishment: Int,
    val questionPicture: String?,
    val signedUrl: URL?,
    val relatedGame: Int?
)