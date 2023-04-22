package no.schmell.backend.dtos.crm

import no.schmell.backend.dtos.auth.SimpleUserDto
import no.schmell.backend.dtos.cms.SimpleGameDto
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

data class TaskFilters(
    val priority: List<TaskPriority>?,
    val status: List<TaskStatus>?,
    val category: List<TaskCategory>?,
    val responsibleUser: Int?,
    val sort: String?,
    val toDate: LocalDateTime?,
    val page: Int,
    val pageSize: Int,
)