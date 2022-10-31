package no.schmell.backend.entities.tasks

import no.schmell.backend.dtos.tasks.CommentDto
import no.schmell.backend.dtos.tasks.CommentListDto
import no.schmell.backend.entities.auth.User
import no.schmell.backend.services.files.FilesService
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="comment")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int?,

    @Column(name = "created_date", nullable = false)
    val createdDate : LocalDateTime,

    @Column(name="comment", nullable = false)
    val comment : String,

    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "application_user_id")
    val writtenBy : User,

    @ManyToOne(cascade = [CascadeType.REMOVE])
    @JoinColumn(name="task_id")
    val relatedTask : Task,
) {
    fun toCommentDto(filesService: FilesService): CommentDto {
        return this.let {
            CommentDto(
                it.id,
                it.createdDate,
                it.comment,
                it.writtenBy.toUserDto(filesService),
                it.relatedTask.toTaskDto(filesService)
            )
        }
    }

    fun toCommentListDto(filesService: FilesService): CommentListDto {
        return this.let {
            CommentListDto(
                it.id,
                it.createdDate,
                it.comment,
                it.writtenBy.toUserDto(filesService),
                it.relatedTask.id!!
            )
        }
    }
}
