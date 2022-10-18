package no.schmell.backend.dtos.tasks

import no.schmell.backend.dtos.auth.UserDto
import java.time.LocalDateTime

data class CreateCommentParams(
    val id : Int?,
    val createdDate : LocalDateTime?,
    val comment : String,
    val writtenBy : Int,
    val relatedTask : Int,
) {
    fun fromCreateToDto(writtenBy: UserDto, relatedTask: TaskDto): CommentDto {
        return this.let {
            CommentDto(
                it.id,
                it.createdDate,
                it.comment,
                writtenBy,
                relatedTask,
            )
        }
    }
}
