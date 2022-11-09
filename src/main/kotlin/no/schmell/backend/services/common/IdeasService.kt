package no.schmell.backend.services.common

import mu.KLogging
import no.schmell.backend.repositories.common.IdeaRepository
import no.schmell.backend.dtos.common.*
import no.schmell.backend.entities.common.Idea
import no.schmell.backend.repositories.auth.UserRepository
import no.schmell.backend.services.files.FilesService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class IdeasService(
    private val ideasRepository: IdeaRepository,
    val userRepository: UserRepository,
    val filesService: FilesService
    ) {

    companion object: KLogging()

    fun getAll(filters: IdeaFilters): List<IdeaDto> {
        var ideas = ideasRepository.findAll()

        if (filters.category != null) ideas = ideas.filter { it.category == filters.category }

        if (filters.createdBy != null) ideas = ideas.filter { it.createdBy.id == filters.createdBy }

        return ideas.map { idea -> idea.toIdeaDto(filesService) }
    }

    fun getById(id: Int): IdeaDto {
        val idea = ideasRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return idea.toIdeaDto(filesService)
    }

    fun create(dto: CreateIdeaDto): IdeaDto {
        val createdBy = userRepository.findByIdOrNull(dto.createdBy) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val createdIdea = Idea(
            null,
            dto.ideaText,
            dto.category,
            createdBy
        )
        return ideasRepository.save(createdIdea).toIdeaDto(filesService)
    }

    fun delete(id: Int) {
        return if (ideasRepository.existsById(id)) {
            ideasRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}