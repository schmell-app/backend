package no.schmell.backend.dtos.cms

import no.schmell.backend.entities.cms.Question
import no.schmell.backend.services.files.FilesService
import java.net.URL

data class QuestionDto(
    val id: Int?,
    var relatedWeek: WeekDto,
    val type: String,
    val questionDescription: String,
    val phase: Int,
    val function: String?,
    val punishment: Int,
    val questionPicture: String?,
    val relatedGame: GameDto,
    val questionPictureUrl: String?
) {
    fun toQuestionEntity(): Question {
        return this.let {
            Question(
                it.id,
                relatedWeek.toWeekEntity(),
                it.type,
                it.questionDescription,
                it.phase,
                it.function,
                it.punishment,
                it.questionPicture,
                relatedGame.toGameEntity()
            )
        }
    }
}