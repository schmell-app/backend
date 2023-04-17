package no.schmell.backend.repositories.common

import no.schmell.backend.entities.cms.Game
import no.schmell.backend.entities.common.GameSession
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime

interface GameSessionRepository : CrudRepository<GameSession, Int> {
    @Query(
        "SELECT count (g) FROM GameSession g WHERE g.createdAt BETWEEN :from AND :to"
    )
    fun countAllInDateRange(
        from: LocalDateTime,
        to: LocalDateTime
    ): Int


    @Query(
        "SELECT g FROM GameSession g WHERE g.createdAt BETWEEN :from AND :to"
    )
    fun findAllInDateRange(
        from: LocalDateTime,
        to: LocalDateTime
    ): List<GameSession>

    @Query(
        "SELECT count (g) FROM GameSession g WHERE g.createdAt BETWEEN :from AND :to"
    )
    fun countAllInDateRangeWithGame(
        from: LocalDateTime,
        to: LocalDateTime,
        relatedGame: Game
    ): Int

    @Query(
        "SELECT COUNT(DISTINCT gs.relatedGame) FROM GameSession gs WHERE gs.createdAt BETWEEN" +
        ":from AND :to"
    )
    fun countUniqueGamesInDateRange(
        from: LocalDateTime,
        to: LocalDateTime
    ): Int
}