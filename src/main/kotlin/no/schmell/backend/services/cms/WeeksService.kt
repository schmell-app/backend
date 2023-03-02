package no.schmell.backend.services.cms

import no.schmell.backend.repositories.cms.WeekRepository
import no.schmell.backend.dtos.cms.week.CreateWeekDto
import no.schmell.backend.dtos.cms.week.WeekDto
import no.schmell.backend.dtos.cms.week.WeekFilters
import no.schmell.backend.entities.cms.Week
import no.schmell.backend.repositories.cms.GameRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class WeeksService(
    private val weekRepository: WeekRepository,
    val gameRepository: GameRepository,
    ) {

    fun getAll(filters: WeekFilters): List<WeekDto> {
        var allWeeks = weekRepository.findAll()

        if (filters.relatedGame != null) allWeeks = allWeeks.filter { it.relatedGame.id == filters.relatedGame }

        if (filters.weekNumber != null) allWeeks = allWeeks.filter { it.weekNumber == filters.weekNumber }

        return allWeeks.map { week -> week.toWeekDto() }
    }

    fun getById(id: Int): WeekDto {
        val week = weekRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return week.toWeekDto()
    }

    fun create(dto: CreateWeekDto): WeekDto {
        return if ((1..52).contains(dto.weekNumber)) {
            val relatedGame = gameRepository.findByIdOrNull(dto.relatedGame) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

            val createdWeek = Week(
                null,
                relatedGame,
                dto.weekNumber,
                null,
            )
            weekRepository.save(createdWeek).toWeekDto()
        } else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Week number must be between 1 and 52")
    }

    fun delete(id: Int) {
        return if (weekRepository.existsById(id)) {
            weekRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}