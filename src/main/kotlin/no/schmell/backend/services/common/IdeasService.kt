package no.schmell.backend.services.common

import mu.KLogging
import no.schmell.backend.repositories.common.IdeaRepository
import no.schmell.backend.dtos.common.*
import no.schmell.backend.lib.files.GenerateObjectSignedUrl
import no.schmell.backend.services.auth.AuthService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class IdeasService(
    private val ideasRepository: IdeaRepository,
    val generateObjectSignedUrl: GenerateObjectSignedUrl,
    val authService: AuthService
    ) {

    companion object: KLogging()

    fun getAll(filters: IdeaFilters): List<IdeaDto> {
        var ideas = ideasRepository.findAll()

        if (filters.category != null) ideas = ideas.filter { it.category == filters.category }

        if (filters.createdBy != null) ideas = ideas.filter { it.createdBy.id == filters.createdBy }

        return ideas.map { idea -> idea.toIdeaDto(generateObjectSignedUrl) }
    }

    fun getById(id: Int): IdeaDto {
        val idea = ideasRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return idea.toIdeaDto(generateObjectSignedUrl)
    }

    fun create(createDto: CreateIdeaParams): IdeaDto {
        val createdBy = authService.getById(createDto.createdBy)
        logger.info { createdBy }
        return ideasRepository.save(createDto.fromCreateToDto(createdBy).toIdeaEntity()).toIdeaDto(generateObjectSignedUrl)
    }

    fun update(id: Int, idea: IdeaDto): IdeaDto {
        return if (ideasRepository.existsById(id)) {
            ideasRepository.save(idea.toIdeaEntity()).toIdeaDto(generateObjectSignedUrl)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun delete(id: Int) {
        return if (ideasRepository.existsById(id)) {
            ideasRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}