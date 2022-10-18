package no.schmell.backend.dtos.tasks

import no.schmell.backend.dtos.auth.UserDto
import java.time.LocalDateTime

data class CommentListDto (
    val id : Int?,
    val createdDate : LocalDateTime,
    val comment : String,
    val writtenBy : UserDto,
    val relatedTask : Int,
)
