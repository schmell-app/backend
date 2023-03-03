package no.schmell.backend.services.crm

import mu.KLogging
import no.schmell.backend.dtos.crm.*
import no.schmell.backend.repositories.crm.TaskRepository
import no.schmell.backend.utils.sortTaskList
import no.schmell.backend.entities.crm.Task
import no.schmell.backend.lib.Mailer
import no.schmell.backend.lib.enums.TaskStatus
import no.schmell.backend.repositories.auth.UserRepository
import no.schmell.backend.repositories.cms.GameRepository
import no.schmell.backend.services.files.FilesService
import org.jobrunr.scheduling.JobScheduler
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class TasksService(
    private val tasksRepository: TaskRepository,
    val userRepository: UserRepository,
    val gameRepository: GameRepository,
    val filesService: FilesService,
    val jobScheduler: JobScheduler) {

    companion object: KLogging()

    @Value("\${spring.mail.api-key}")
    lateinit var apiKey: String

    @Value("\${spring.mail.admin-url}")
    lateinit var adminUrl: String

    fun getAll(filters: TaskFilters): TaskPaginatedResponse {
        var tasks = tasksRepository.findAllByPriorityIsInAndCategoryIsInAndCategoryIsInAndResponsibleUserIdAndDeadlineBefore(
            filters.priority,
            filters.status,
            filters.category,
            filters.responsibleUser,
            filters.toDate,
            PageRequest.of(filters.page - 1, filters.pageSize)
        )
        if (filters.sort != null) tasks = sortTaskList(tasks, filters.sort)


        val total = tasksRepository.countAllByPriorityIsInAndCategoryIsInAndCategoryIsInAndResponsibleUserIdAndDeadlineBefore(
            filters.priority,
            filters.status,
            filters.category,
            filters.responsibleUser,
            filters.toDate
        )

        return TaskPaginatedResponse(
            tasks = tasks.map { it.toTaskDto(filesService) },
            total = total,
            page = filters.page,
            lastPage = (total / filters.pageSize) + 1
        )
    }

    fun getById(id: Int): TaskDto {
        val task = tasksRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return task.toTaskDto(filesService)
    }

    fun create(dto: CreateTaskDto): TaskDto {
        val createdTaskDto = Task(
            null,
            LocalDateTime.now(),
            dto.title,
            dto.description,
            dto.status ?: TaskStatus.PENDING,
            dto.deadline,
            dto.category,
            dto.priority,
            userRepository.findByIdOrNull(dto.responsibleUser) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"),
            gameRepository.findByIdOrNull(dto.relatedGame ?: 0),
            LocalDateTime.now()
        )
        val createdTask = tasksRepository.save(createdTaskDto)
        val mailer = Mailer(apiKey, adminUrl)
        mailer.sendTaskCreatedEmail(getUserEmails(), createdTask)

        jobScheduler.schedule(
            createdTask.deadline.minusHours(24)
        ) { checkIfTaskFinished(createdTask.id!!) }

        return createdTask.toTaskDto(filesService)
    }

    fun update(id: Int, dto: UpdateTaskDto): TaskDto {
        val taskToUpdate = tasksRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        val updatedTask = Task(
            taskToUpdate.id,
            taskToUpdate.createdDateTime,
            taskToUpdate.title,
            taskToUpdate.description,
            dto.status ?: taskToUpdate.status,
            dto.deadline ?: taskToUpdate.deadline,
            taskToUpdate.category,
            taskToUpdate.priority,
            taskToUpdate.responsibleUser,
            gameRepository.findByIdOrNull(dto.relatedGame ?: 0) ?: taskToUpdate.relatedGame,
            LocalDateTime.now()
        )

        return tasksRepository.save(updatedTask).toTaskDto(filesService)
    }

    fun delete(id: Int) {
        return if (tasksRepository.existsById(id)) {
            tasksRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    private fun getUserEmails(): List<String> {
        return userRepository.findAll().filter { it.alertsForTasks }.map { it.email }
    }

    fun checkIfTaskFinished(taskId: Int) {
        val task = tasksRepository.findByIdOrNull(taskId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        if (task.status != TaskStatus.DONE) {
            val mailer = Mailer(apiKey, adminUrl)
            mailer.sendTaskReachingDeadline(arrayListOf(task.responsibleUser.email), task)
        }
    }
}