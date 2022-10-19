package no.schmell.backend.entities.tasks

import no.schmell.backend.dtos.tasks.TaskDto
import no.schmell.backend.entities.auth.User
import no.schmell.backend.entities.cms.Game
import no.schmell.backend.lib.enums.TaskCategory
import no.schmell.backend.lib.enums.TaskPriority
import no.schmell.backend.lib.enums.TaskStatus
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "task")
class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,

    @Column(name = "created_date_time", nullable = false)
    val createdDateTime: LocalDateTime,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "description", nullable = false)
    val description: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: TaskStatus,

    @Column(name = "deadline", nullable = false)
    val deadline: LocalDateTime,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: TaskCategory,

    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    val priority: TaskPriority,

    @ManyToOne(cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "application_user_id")
    val responsibleUser: User,

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "game_id", nullable = true)
    val relatedGame: Game?,

    @Column(name = "last_updated", nullable = false)
    val lastUpdated : LocalDateTime,
) {
    fun toTaskDto(): TaskDto {
        return this.let {
            TaskDto(
                it.id,
                it.createdDateTime,
                it.title,
                it.description,
                it.status,
                it.deadline,
                it.category,
                it.priority,
                it.responsibleUser.toUserDto(),
                it.relatedGame?.toGameDto(),
                it.lastUpdated
            )
        }
    }
}
