package no.schmell.backend.dtos.tasks

import no.schmell.backend.dtos.auth.SimpleUserDto
import java.time.LocalDateTime

data class CommentDto(
    val id : Int?,
    val createdDate : LocalDateTime,
    val comment : String,
    val writtenBy : SimpleUserDto,
    val relatedTask : Int,
)

data class CreateCommentDto(
    val comment: String,
    val writtenBy: Int,
    val relatedTask: Int
)
