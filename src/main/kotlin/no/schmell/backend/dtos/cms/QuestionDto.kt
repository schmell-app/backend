package no.schmell.backend.dtos.cms

import no.schmell.backend.entities.cms.Question
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
    val signedUrl: URL?,
    val relatedGame: GameDto,
) {
    fun toQuestionEntity(): Question {
        return this.let {
            Question(
                it.id ?: null,
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