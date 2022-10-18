package no.schmell.backend.repositories.common

import no.schmell.backend.entities.common.Idea
import org.springframework.data.repository.CrudRepository

interface IdeaRepository : CrudRepository<Idea, Int>