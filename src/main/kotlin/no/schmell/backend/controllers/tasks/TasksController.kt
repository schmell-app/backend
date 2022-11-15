package no.schmell.backend.controllers.tasks

import no.schmell.backend.dtos.tasks.CreateTaskDto
import no.schmell.backend.dtos.tasks.TaskDto
import no.schmell.backend.dtos.tasks.TaskFilters
import no.schmell.backend.dtos.tasks.UpdateTaskDto
import no.schmell.backend.lib.enums.TaskCategory
import no.schmell.backend.lib.enums.TaskPriority
import no.schmell.backend.lib.enums.TaskStatus
import no.schmell.backend.services.tasks.TasksService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@RestController
@RequestMapping("/v2/tasks")
@CrossOrigin("http://localhost:3000")
class TasksController(val tasksService: TasksService) {

    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    fun getTask(@PathVariable("id") id: String): TaskDto = tasksService.getById(id.toInt())

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun getTasks(
        @RequestParam(value = "status", required = false) status: String?,
        @RequestParam(value = "priority", required = false) priority: String?,
        @RequestParam(value = "category", required = false) category: String?,
        @RequestParam(value = "responsibleUser", required = false) responsibleUser: String?,
        @RequestParam(value = "sort", required = false) sort: String?,
        @RequestParam(value = "toDate", required = false) toDate: String?,
    ): List<TaskDto> =
        tasksService.getAll(
            TaskFilters(
                priority?.split("+")?.map { TaskPriority.valueOf(it) },
                status?.split("+")?.map { TaskStatus.valueOf(it) },
                category?.split("+")?.map { TaskCategory.valueOf(it) },
                responsibleUser?.toInt(),
                sort,
                toDate?.let { LocalDateTime.parse(toDate, DateTimeFormatter.ISO_DATE_TIME) }
            )
        )

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(@RequestBody(required = true) dto: CreateTaskDto): TaskDto = tasksService.create(dto)

    @PatchMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    fun updateTask(@PathVariable("id") id: String, @RequestBody(required = true) dto: UpdateTaskDto): TaskDto = tasksService.update(id.toInt(), dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTask(@PathVariable("id") id: String) = tasksService.delete(id.toInt())
}