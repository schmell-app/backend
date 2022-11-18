package no.schmell.backend.dtos.tasks

import no.schmell.backend.dtos.auth.SimpleUserDto
import no.schmell.backend.dtos.cms.game.SimpleGameDto
import no.schmell.backend.entities.tasks.Task
import no.schmell.backend.lib.enums.TaskCategory
import no.schmell.backend.lib.enums.TaskPriority
import no.schmell.backend.lib.enums.TaskStatus
import java.time.LocalDateTime

data class TaskDto(
    val id : Int?,
    val createdDateTime : LocalDateTime,
    val title : String,
    val description : String,
    val status : TaskStatus,
    val deadline : LocalDateTime,
    val category : TaskCategory,
    val priority : TaskPriority,
    val responsibleUser : SimpleUserDto,
    val relatedGame : SimpleGameDto?,
    val lastUpdated : LocalDateTime,
)
data class TaskPaginatedResponse (
    val tasks: List<TaskDto>,
    val total: Int,
    val page: Int,
    val lastPage: Int,
)

data class CreateTaskDto(
    val title: String,
    val description: String,
    val status: TaskStatus?,
    val deadline: LocalDateTime,
    val category: TaskCategory,
    val priority: TaskPriority,
    val responsibleUser: Int,
    val relatedGame: Int?,
)

data class UpdateTaskDto(
    val status: TaskStatus?,
    val deadline: LocalDateTime?,
    val relatedGame: Int?
)