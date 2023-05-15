package no.schmell.backend.repositories.cms

import no.schmell.backend.entities.cms.Question
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface QuestionRepository : CrudRepository<Question, Int> {
    @Query("SELECT q FROM Question q WHERE ((:relatedGame) IS NULL OR q.relatedGame.id = (:relatedGame))")
    fun findAllByRelatedGameId(relatedGame: Int?): List<Question>
}