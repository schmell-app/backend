package no.schmell.backend.repositories.tasks

import no.schmell.backend.entities.tasks.Task
import org.springframework.data.repository.CrudRepository

interface TaskRepository : CrudRepository<Task, Int>