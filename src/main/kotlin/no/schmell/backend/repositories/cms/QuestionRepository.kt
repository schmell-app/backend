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
            "AND ((:hasDislikes) IS NULL OR (:hasDislikes) = true AND q.dislikesCount > 0 OR (:hasDislikes) = false AND q.dislikesCount = 0)" +
            "AND ((:dislikesGreaterThan) IS NULL OR q.dislikesCount > (:dislikesGreaterThan))" +
            "ORDER BY q.id ASC")
    fun findAllByFilters(
        relatedGame: Int?,
        questionType: Int?,
        questionDescription: String?,
        hasDislikes: Boolean?,
        dislikesGreaterThan: Int?,
        pageable: Pageable
    ): List<Question>

    @Query("SELECT count (q) FROM Question q WHERE " +
            "((:questionType) IS NULL OR q.questionType.id = (:questionType))" +
            "AND ((:questionDescription) IS NULL OR q.questionDescription LIKE %:questionDescription%)" +
            "AND ((:relatedGame) IS NULL OR q.relatedGame.id = (:relatedGame))" +
            "AND ((:hasDislikes) IS NULL OR (:hasDislikes) = true AND q.dislikesCount > 0 OR (:hasDislikes) = false AND q.dislikesCount = 0)" +
            "AND ((:dislikesGreaterThan) IS NULL OR q.dislikesCount > (:dislikesGreaterThan))")
    fun countAllByFilters(
        relatedGame: Int?,
        questionType: Int?,
        questionDescription: String?,
        hasDislikes: Boolean?,
        dislikesGreaterThan: Int?
    ): Int
}