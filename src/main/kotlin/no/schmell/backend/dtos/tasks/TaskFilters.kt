package no.schmell.backend.dtos.tasks

import no.schmell.backend.lib.enums.TaskCategory
import no.schmell.backend.lib.enums.TaskPriority
import no.schmell.backend.lib.enums.TaskStatus
import java.time.LocalDateTime

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