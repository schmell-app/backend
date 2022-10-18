package no.schmell.backend.dtos.tasks

import no.schmell.backend.dtos.auth.UserDto
import no.schmell.backend.entities.tasks.Comment
import java.time.LocalDateTime

data class CommentDto(
    val id : Int?,
    val createdDate : LocalDateTime?,
    val comment : String,
    val writtenBy : UserDto,
    val relatedTask : TaskDto,
) {
    fun toCommentEntity(): Comment {
        val createdDate = LocalDateTime.now()

        return this.let {
            Comment(
                it.id,
                it.createdDate ?: createdDate,
                it.comment,
                it.writtenBy.toUserEntity(),
                it.relatedTask.toTaskEntity()
            )
        }
    }

}
