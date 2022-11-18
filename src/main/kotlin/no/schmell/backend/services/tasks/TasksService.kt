package no.schmell.backend.services.tasks

import mu.KLogging
import no.schmell.backend.repositories.tasks.TaskRepository
import no.schmell.backend.utils.sortTaskList
import no.schmell.backend.dtos.tasks.*
import no.schmell.backend.entities.tasks.Task
import no.schmell.backend.lib.enums.TaskStatus
import no.schmell.backend.repositories.auth.UserRepository
import no.schmell.backend.repositories.cms.GameRepository
import no.schmell.backend.services.cms.GamesService
import no.schmell.backend.services.files.FilesService
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
    val filesService: FilesService) {

    companion object: KLogging()

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
        val createdTask = Task(
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
        return tasksRepository.save(createdTask).toTaskDto(filesService)
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
}