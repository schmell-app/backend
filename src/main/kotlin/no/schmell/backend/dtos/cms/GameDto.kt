package no.schmell.backend.dtos.cms

import no.schmell.backend.entities.cms.Game
import no.schmell.backend.lib.enums.GameStatus
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime

data class GameDto(
    val id : Int?,
    val name : String,
    val description : String,
    val lastUpdated : LocalDateTime?,
    val status : GameStatus?,
    val logo : String?,
    val releaseDate: LocalDate?,
    val logoUrl: String?
) {

    fun toGameEntity(): Game {
        val today = LocalDateTime.now()
        val status = GameStatus.DEVELOPMENT

        return this.let {
            Game(
                it.id,
                it.name,
                it.description,
                it.lastUpdated ?: today,
                it.status ?: status,
                it.logo,
                it.releaseDate,
            )
        }
    }
}