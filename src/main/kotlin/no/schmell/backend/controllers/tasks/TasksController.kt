package no.schmell.backend.controllers.tasks

import no.schmell.backend.dtos.tasks.CreateTaskParams
import no.schmell.backend.dtos.tasks.TaskDto
import no.schmell.backend.dtos.tasks.TaskFilters
import no.schmell.backend.lib.enums.TaskCategory
import no.schmell.backend.lib.enums.TaskPriority
import no.schmell.backend.lib.enums.TaskStatus
import no.schmell.backend.services.tasks.TasksService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v2/tasks/task")
class TasksController(val tasksService: TasksService) {

    @GetMapping("/{id}/")
    fun getTask(@PathVariable("id") id: String): TaskDto = tasksService.getById(id.toInt())

    @GetMapping("")
    fun getTasks(
        @RequestParam(value = "status", required = false) status: TaskStatus?,
        @RequestParam(value = "priority", required = false) priority: TaskPriority?,
        @RequestParam(value = "id", required = false) id: Int?,
        @RequestParam(value = "category", required = false) category: TaskCategory?,
        @RequestParam(value = "responsibleUser", required = false) responsibleUser: Int?,
        @RequestParam(value = "sort", required = false) sort: String?,
    ): List<TaskDto> =
        tasksService.getAll(
            TaskFilters(
            priority,
            status,
            id,
            category,
            responsibleUser,
            sort
        ))

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(@RequestBody(required = true) dto: CreateTaskParams): TaskDto = tasksService.create(dto)

    @PutMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    fun updateTask(@PathVariable("id") id: String, @RequestBody(required = true) dto: TaskDto): TaskDto = tasksService.update(id.toInt(), dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTask(@PathVariable("id") id: String) = tasksService.delete(id.toInt())
}