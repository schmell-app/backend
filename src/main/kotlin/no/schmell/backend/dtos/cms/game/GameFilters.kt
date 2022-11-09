package no.schmell.backend.dtos.cms.game

import no.schmell.backend.lib.enums.GameStatus

data class GameFilters(
    val name : String?,
    val status : GameStatus?,
)
