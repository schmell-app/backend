package no.schmell.backend.dtos.common

import no.schmell.backend.dtos.cms.GameDto
import java.time.LocalDateTime

data class GameSessionDto(
    val id : Int?,
    val createdAt : LocalDateTime,
    val relatedGame : GameDto,
)

data class CreateGameSession(
    val relatedGameId : Int,
)