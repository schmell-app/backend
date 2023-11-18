package no.schmell.backend.repositories.cms

import no.schmell.backend.entities.cms.Question
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface QuestionRepository : CrudRepository<Question, Int> {
    @Query("SELECT q FROM Question q WHERE ((:relatedGame) IS NULL OR q.relatedGame.id = (:relatedGame))")
    fun findAllByRelatedGameId(relatedGame: Int?): List<Question>

    @Query("SELECT q FROM Question q WHERE " +
            "((:questionType) IS NULL OR q.questionType.id = (:questionType))" +
            "AND ((:questionDescription) IS NULL OR q.questionDescription LIKE %:questionDescription%)" +
            "AND ((:relatedGame) IS NULL OR q.relatedGame.id = (:relatedGame))" +
            "AND ((:activeWeeks) IS NULL OR q.activeWeeks IN (:activeWeeks))" +
            "ORDER BY q.id ASC")
    fun findAllByFilters(
        relatedGame: Int?,
        questionType: Int?,
        questionDescription: String?,
        activeWeeks: List<Int>?,
        pageable: Pageable
    ): List<Question>

    @Query("SELECT count (q) FROM Question q WHERE " +
            "((:questionType) IS NULL OR q.questionType.id = (:questionType))" +
            "AND ((:questionDescription) IS NULL OR q.questionDescription LIKE %:questionDescription%)" +
            "AND ((:relatedGame) IS NULL OR q.relatedGame.id = (:relatedGame))" +
            "AND ((:activeWeeks) IS NULL OR q.activeWeeks IN (:activeWeeks))")
    fun countAllByFilters(
        relatedGame: Int?,
        questionType: Int?,
        questionDescription: String?,
        activeWeeks: List<Int>?
    ): Int
}