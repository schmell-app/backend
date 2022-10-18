package no.schmell.backend.repositories.cms

import no.schmell.backend.entities.cms.Question
import org.springframework.data.repository.CrudRepository

interface QuestionRepository : CrudRepository<Question, Int>