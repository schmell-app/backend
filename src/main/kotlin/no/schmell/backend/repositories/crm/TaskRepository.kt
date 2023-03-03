package no.schmell.backend.repositories.crm

import no.schmell.backend.entities.crm.Task
import no.schmell.backend.lib.enums.TaskCategory
import no.schmell.backend.lib.enums.TaskPriority
import no.schmell.backend.lib.enums.TaskStatus
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime

interface TaskRepository : CrudRepository<Task, Int> {
    @Query("SELECT t FROM Task t WHERE " +
            "((:priority) IS NULL OR t.priority IN (:priority)) AND " +
            "((:status) IS NULL OR t.status IN (:status)) AND " +
            "((:category) IS NULL OR t.category IN (:category)) AND " +
            "((:responsibleUser) IS NULL OR t.responsibleUser.id = (:responsibleUser)) AND " +
            "(cast(:toDate as LocalDateTime) IS NULL OR t.deadline <= (:toDate))")
    fun findAllByPriorityIsInAndCategoryIsInAndCategoryIsInAndResponsibleUserIdAndDeadlineBefore(
        priority: List<TaskPriority>?,
        status: List<TaskStatus>?,
        category: List<TaskCategory>?,
        responsibleUser: Int?,
        toDate: LocalDateTime?,
        pageable: Pageable
    ): List<Task>

    @Query("SELECT count (t) FROM Task t WHERE " +
            "((:priority) IS NULL OR t.priority IN (:priority)) AND " +
            "((:status) IS NULL OR t.status IN (:status)) AND " +
            "((:category) IS NULL OR t.category IN (:category)) AND " +
            "((:responsibleUser) IS NULL OR t.responsibleUser.id = (:responsibleUser)) AND " +
            "(cast(:toDate as LocalDateTime) IS NULL OR t.deadline <= (:toDate))")
    fun countAllByPriorityIsInAndCategoryIsInAndCategoryIsInAndResponsibleUserIdAndDeadlineBefore(
        priority: List<TaskPriority>?,
        status: List<TaskStatus>?,
        category: List<TaskCategory>?,
        responsibleUser: Int?,
        toDate: LocalDateTime?
    ): Int
}