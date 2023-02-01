package no.schmell.backend.repositories.cms

import no.schmell.backend.entities.cms.QuestionFunction
import org.springframework.data.repository.CrudRepository

interface QuestionFunctionRepository : CrudRepository<QuestionFunction, Int>