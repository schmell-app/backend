package no.schmell.backend.repositories.cms

import no.schmell.backend.entities.cms.Game
import org.springframework.data.repository.CrudRepository

interface GameRepository : CrudRepository<Game, Int>