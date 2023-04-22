package no.schmell.backend.repositories.cms

import no.schmell.backend.entities.cms.QuestionType
import org.springframework.data.repository.CrudRepository

interface QuestionTypeRepository : CrudRepository<QuestionType, Int> {
    fun findAllByNameContains(searchString: String) : List<QuestionType>
}