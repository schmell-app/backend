package no.schmell.backend.dtos.cms

import no.schmell.backend.entities.cms.Game
import no.schmell.backend.lib.enums.GameStatus
import org.springframework.web.client.HttpServerErrorException.InternalServerError
import java.time.LocalDateTime

data class GameDto(
    var id : Int?,
    val name : String,
    val description : String,
    val lastUpdated : LocalDateTime,
    val status : GameStatus,
    val logo : String?,
    val logoUrl: String?,
    val isFamilyFriendly: Boolean
)

data class CreateGameDto(
    val name: String,
    val description: String,
    val status: GameStatus?,
    val isFamilyFriendly: Boolean
)

data class UpdateGameDto(
    val description: String?,
    val status: GameStatus?,
    val isFamilyFriendly: Boolean?
)

data class GameFilters(
    val name : String?,
    val status : GameStatus?,
)
data class SimpleGameDto(
    val id: Int?,
    val name: String,
)