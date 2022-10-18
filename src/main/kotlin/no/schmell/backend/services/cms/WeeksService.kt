package no.schmell.backend.services.cms

import no.schmell.backend.repositories.cms.WeekRepository
import no.schmell.backend.dtos.cms.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class WeeksService(
    private val weekRepository: WeekRepository,
    val gamesService: GamesService
    ) {

    @Value("\${gcp.config.file}")
    lateinit var gcpConfigFile: String

    @Value("\${gcp.project.id}")
    lateinit var gcpProjectId: String

    @Value("\${gcp.bucket.id}")
    lateinit var gcpBucketId: String

    fun getAll(filters: WeekFilters): List<WeekDto> {
        var allWeeks = weekRepository.findAll()

        if (filters.relatedGame != null) allWeeks = allWeeks.filter { it.relatedGame.id == filters.relatedGame }

        if (filters.weekNumber != null) allWeeks = allWeeks.filter { it.weekNumber == filters.weekNumber }

        return allWeeks.map { week -> week.toWeekDto(gcpProjectId, gcpBucketId, gcpConfigFile) }
    }

    fun getById(id: Int): WeekDto {
        val week = weekRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return week.toWeekDto(gcpProjectId, gcpBucketId, gcpConfigFile)
    }

    fun create(dto: CreateWeekParams): WeekDto {
        return if ((1..52).contains(dto.weekNumber)) {
            val game = gamesService.getById(dto.relatedGame)
            weekRepository.save(dto.fromCreateToDto(game).toWeekEntity()).toWeekDto(gcpProjectId, gcpBucketId, gcpConfigFile)
        } else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Week number must be between 1 and 52")
    }

    fun update(id: Int, week: WeekDto): WeekDto {
        return if (weekRepository.existsById(id)) {
            weekRepository.save(week.toWeekEntity()).toWeekDto(gcpProjectId, gcpBucketId, gcpConfigFile)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun delete(id: Int) {
        return if (weekRepository.existsById(id)) {
            weekRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}