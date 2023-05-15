package no.schmell.backend.repositories.cms

import no.schmell.backend.entities.cms.Game
import no.schmell.backend.lib.enums.GameStatus
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface GameRepository : CrudRepository<Game, Int> {
    @Query(
        "SELECT g FROM Game g WHERE g.status = :status"
    )
    fun findAllDeployedGames(
        status: GameStatus
    ): List<Game>
}