package no.schmell.backend.dtos.cms

import no.schmell.backend.entities.cms.Week

data class WeekDto(
    val id : Int?,
    var relatedGame : GameDto,
    val weekNumber: Int,
) {
    fun toWeekEntity(): Week {
        return this.let {
            Week(
                it.id ?: null,
                it.relatedGame.toGameEntity(),
                it.weekNumber
            )
        }
    }
}
