package no.schmell.backend.services.cms

import no.schmell.backend.dtos.cms.QuestionTypeDto
import no.schmell.backend.dtos.cms.QuestionTypeFilter
import no.schmell.backend.dtos.cms.UpdateQuestionTypeDto
import no.schmell.backend.entities.cms.QuestionType
import no.schmell.backend.repositories.cms.QuestionTypeRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class QuestionTypeService(
    val questionTypeRepository: QuestionTypeRepository
) {
    fun create(dto: QuestionTypeDto): QuestionTypeDto {
        return questionTypeRepository.save(
            QuestionType(
                null,
                dto.name,
                dto.hexColor,
                dto.hint
            )
        ).toQuestionTypeDto()
    }

    fun getById(id: Int): QuestionType {
        return questionTypeRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun get(filters: QuestionTypeFilter): List<QuestionTypeDto> {
        if (filters.nameSearch != null)
            return questionTypeRepository.findAllByNameContains(filters.nameSearch).map { it.toQuestionTypeDto() }

        return questionTypeRepository.findAll().map { it.toQuestionTypeDto() }
    }

    fun update(id: Int, dto: UpdateQuestionTypeDto): QuestionTypeDto {
        val questionTypeToUpdate = this.getById(id)

        return questionTypeRepository.save(
            QuestionType(
                questionTypeToUpdate.id,
                dto.name ?: questionTypeToUpdate.name,
                dto.hexColor ?: questionTypeToUpdate.hexColor,
                dto.hint ?: questionTypeToUpdate.hint
            )
        ).toQuestionTypeDto()
    }

    fun delete(id: Int) {
        return if (questionTypeRepository.existsById(id)) {
            questionTypeRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}