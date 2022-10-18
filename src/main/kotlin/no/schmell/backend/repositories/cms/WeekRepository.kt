package no.schmell.backend.repositories.cms

import no.schmell.backend.entities.cms.Week
import org.springframework.data.repository.CrudRepository

interface WeekRepository : CrudRepository<Week, Int>