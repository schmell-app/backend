package no.schmell.backend.services.tasks

import no.schmell.backend.repositories.tasks.CommentRepository
import no.schmell.backend.dtos.tasks.*
import no.schmell.backend.entities.tasks.Comment
import no.schmell.backend.repositories.auth.UserRepository
import no.schmell.backend.repositories.tasks.TaskRepository
import no.schmell.backend.services.files.FilesService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class CommentsService(
    private val commentsRepository: CommentRepository,
    val userRepository: UserRepository,
    val tasksRepository: TaskRepository,
    val filesService: FilesService
) {

    fun getAll(filters: CommentFilters): List<CommentDto> {
        var comments = commentsRepository.findAll()

        if (filters.relatedTask != null) comments = comments.filter { it.relatedTask.id == filters.relatedTask }

        return comments.sortedByDescending { it.createdDate }.map { comment -> comment.toCommentDto(filesService) }
    }

    fun getById(id: Int): CommentDto {
        val comment = commentsRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return comment.toCommentDto(filesService)
    }

    fun create(dto: CreateCommentDto): CommentDto {
        val writtenBy = userRepository.findByIdOrNull(dto.writtenBy) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val relatedTask = tasksRepository.findByIdOrNull(dto.relatedTask) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        val createdComment = Comment(
            null,
            LocalDateTime.now(),
            dto.comment,
            writtenBy,
            relatedTask
        )
        return commentsRepository.save(createdComment).toCommentDto(filesService)
    }

    fun delete(id: Int) {
        return if (commentsRepository.existsById(id)) {
            commentsRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}