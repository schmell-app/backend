package no.schmell.backend.services.tasks

import no.schmell.backend.repositories.tasks.CommentRepository
import no.schmell.backend.dtos.tasks.*
import no.schmell.backend.services.auth.AuthService
import no.schmell.backend.services.files.FilesService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CommentsService(
    private val commentsRepository: CommentRepository,
    val authService: AuthService,
    val tasksService: TasksService,
    val filesService: FilesService
) {

    fun getAll(filters: CommentFilters): List<CommentListDto> {
        var comments = commentsRepository.findAll()

        if (filters.relatedTask != null) comments = comments.filter { it.relatedTask.id == filters.relatedTask }

        return comments.sortedByDescending { it.createdDate }.map { comment -> comment.toCommentListDto(filesService) }
    }

    fun getById(id: Int): CommentDto {
        val comment = commentsRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return comment.toCommentDto(filesService)
    }

    fun create(createDto: CreateCommentParams): CommentDto {
        val writtenBy = authService.getById(createDto.writtenBy)
        val relatedTask = tasksService.getById(createDto.relatedTask)

        return commentsRepository.save(
            createDto.fromCreateToDto(writtenBy, relatedTask).toCommentEntity()
        ).toCommentDto(filesService)
    }

    fun update(id: Int, comment: CommentDto): CommentDto {
        return if (commentsRepository.existsById(id)) {
            commentsRepository.save(comment.toCommentEntity()).toCommentDto(filesService)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun delete(id: Int) {
        return if (commentsRepository.existsById(id)) {
            commentsRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}