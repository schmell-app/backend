package no.schmell.backend.services.tasks

import no.schmell.backend.repositories.tasks.TaskRepository
import no.schmell.backend.utils.sortTaskList
import no.schmell.backend.dtos.tasks.*
import no.schmell.backend.services.auth.AuthService
import no.schmell.backend.services.cms.GamesService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TasksService(
    private val tasksRepository: TaskRepository,
    val authService: AuthService,
    val gamesService: GamesService) {

    @Value("\${gcp.config.file}")
    lateinit var gcpConfigFile: String

    @Value("\${gcp.project.id}")
    lateinit var gcpProjectId: String

    @Value("\${gcp.bucket.id}")
    lateinit var gcpBucketId: String

    fun getAll(filters: TaskFilters): List<TaskDto> {
        var tasks = tasksRepository.findAll()

        if (filters.status != null) tasks = tasks.filter { it.status == filters.status }
        if (filters.priority != null) tasks = tasks.filter { it.priority == filters.priority }
        if (filters.taskId != null) tasks = tasks.filter { it.id == filters.taskId }
        if (filters.category != null) tasks = tasks.filter { it.category == filters.category }
        if (filters.responsibleUser != null) tasks = tasks.filter { it.responsibleUser.id == filters.responsibleUser }
        if (filters.sort != null) tasks = sortTaskList(tasks, filters.sort)

        return tasks.map { task -> task.toTaskDto(gcpProjectId, gcpBucketId, gcpConfigFile) }
    }

    fun getById(id: Int): TaskDto {
        val task = tasksRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return task.toTaskDto(gcpProjectId, gcpBucketId, gcpConfigFile)
    }

    fun create(createDto: CreateTaskParams): TaskDto {
        val responsibleUser = authService.getById(createDto.responsibleUser)
        val relatedGame = createDto.relatedGame?.let { gamesService.getById(it) }

        return tasksRepository.save(
            createDto.fromCreateToDto(responsibleUser, relatedGame).toTaskEntity()
        ).toTaskDto(gcpProjectId, gcpBucketId, gcpConfigFile)
    }

    fun update(id: Int, task: TaskDto): TaskDto {
        return if (tasksRepository.existsById(id)) {
            tasksRepository.save(task.toTaskEntity()).toTaskDto(gcpProjectId, gcpBucketId, gcpConfigFile)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun delete(id: Int) {
        return if (tasksRepository.existsById(id)) {
            tasksRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}