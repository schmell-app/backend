package no.schmell.backend.dtos.tasks

import no.schmell.backend.lib.enums.TaskCategory
import no.schmell.backend.lib.enums.TaskPriority
import no.schmell.backend.lib.enums.TaskStatus

data class TaskFilters(
    val priority: TaskPriority?,
    val status: TaskStatus?,
    val taskId: Int?,
    val category: TaskCategory?,
    val responsibleUser: Int?,
    val sort: String?,
)