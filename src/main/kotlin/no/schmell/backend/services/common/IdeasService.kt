package no.schmell.backend.services.common

import mu.KLogging
import no.schmell.backend.repositories.common.IdeaRepository
import no.schmell.backend.dtos.common.*
import no.schmell.backend.services.auth.AuthService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class IdeasService(
    private val ideasRepository: IdeaRepository,
    val authService: AuthService
    ) {

    companion object: KLogging()

    @Value("\${gcp.config.file}")
    lateinit var gcpConfigFile: String

    @Value("\${gcp.project.id}")
    lateinit var gcpProjectId: String

    @Value("\${gcp.bucket.id}")
    lateinit var gcpBucketId: String

    fun getAll(filters: IdeaFilters): List<IdeaDto> {
        var ideas = ideasRepository.findAll()

        if (filters.category != null) ideas = ideas.filter { it.category == filters.category }

        if (filters.createdBy != null) ideas = ideas.filter { it.createdBy.id == filters.createdBy }

        return ideas.map { idea -> idea.toIdeaDto(gcpProjectId, gcpBucketId, gcpConfigFile) }
    }

    fun getById(id: Int): IdeaDto {
        val idea = ideasRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return idea.toIdeaDto(gcpProjectId, gcpBucketId, gcpConfigFile)
    }

    fun create(createDto: CreateIdeaParams): IdeaDto {
        val createdBy = authService.getById(createDto.createdBy)
        logger.info { createdBy }
        return ideasRepository.save(createDto.fromCreateToDto(createdBy).toIdeaEntity()).toIdeaDto(gcpProjectId, gcpBucketId, gcpConfigFile)
    }

    fun update(id: Int, idea: IdeaDto): IdeaDto {
        return if (ideasRepository.existsById(id)) {
            ideasRepository.save(idea.toIdeaEntity()).toIdeaDto(gcpProjectId, gcpBucketId, gcpConfigFile)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun delete(id: Int) {
        return if (ideasRepository.existsById(id)) {
            ideasRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}