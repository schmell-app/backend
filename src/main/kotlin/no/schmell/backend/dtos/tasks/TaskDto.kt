package no.schmell.backend.dtos.tasks

import no.schmell.backend.dtos.auth.UserDto
import no.schmell.backend.dtos.cms.GameDto
import no.schmell.backend.entities.tasks.Task
import no.schmell.backend.lib.enums.TaskCategory
import no.schmell.backend.lib.enums.TaskPriority
import no.schmell.backend.lib.enums.TaskStatus
import java.time.LocalDateTime

data class TaskDto(
    val id : Int?,
    val createdDateTime : LocalDateTime?,
    val title : String,
    val description : String,
    val status : TaskStatus?,
    val deadline : LocalDateTime,
    val category : TaskCategory,
    val priority : TaskPriority,
    val responsibleUser : UserDto,
    val relatedGame : GameDto?,
    val lastUpdated : LocalDateTime?,
) {

    fun toTaskEntity(): Task {
        val createdDateTime = LocalDateTime.now()
        val status = TaskStatus.PENDING
        val lastUpdated = LocalDateTime.now()

        return this.let {
            Task(
                it.id,
                it.createdDateTime ?: createdDateTime,
                it.title,
                it.description,
                it.status ?: status,
                it.deadline,
                it.category,
                it.priority,
                it.responsibleUser.toUserEntity(),
                it.relatedGame?.toGameEntity(),
                it.lastUpdated ?: lastUpdated
            )
        }
    }
}