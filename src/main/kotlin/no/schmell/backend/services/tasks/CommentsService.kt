package no.schmell.backend.services.tasks

import no.schmell.backend.repositories.tasks.CommentRepository
import no.schmell.backend.dtos.tasks.*
import no.schmell.backend.services.auth.AuthService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CommentsService(
    private val commentsRepository: CommentRepository,
    val authService: AuthService,
    val tasksService: TasksService
) {

    @Value("\${gcp.config.file}")
    lateinit var gcpConfigFile: String

    @Value("\${gcp.project.id}")
    lateinit var gcpProjectId: String

    @Value("\${gcp.bucket.id}")
    lateinit var gcpBucketId: String


    fun getAll(filters: CommentFilters): List<CommentListDto> {
        var comments = commentsRepository.findAll()

        if (filters.relatedTask != null) comments = comments.filter { it.relatedTask.id == filters.relatedTask }

        return comments.sortedByDescending { it.createdDate }.map { comment -> comment.toCommentListDto(gcpProjectId, gcpBucketId, gcpConfigFile) }
    }

    fun getById(id: Int): CommentDto {
        val comment = commentsRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return comment.toCommentDto(gcpProjectId, gcpBucketId, gcpConfigFile)
    }

    fun create(createDto: CreateCommentParams): CommentDto {
        val writtenBy = authService.getById(createDto.writtenBy)
        val relatedTask = tasksService.getById(createDto.relatedTask)

        return commentsRepository.save(
            createDto.fromCreateToDto(writtenBy, relatedTask).toCommentEntity()
        ).toCommentDto(gcpProjectId, gcpBucketId, gcpConfigFile)
    }

    fun update(id: Int, comment: CommentDto): CommentDto {
        return if (commentsRepository.existsById(id)) {
            commentsRepository.save(comment.toCommentEntity()).toCommentDto(gcpProjectId, gcpBucketId, gcpConfigFile)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun delete(id: Int) {
        return if (commentsRepository.existsById(id)) {
            commentsRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}