package no.schmell.backend.entities.cms

import no.schmell.backend.dtos.cms.question.QuestionDto
import no.schmell.backend.services.files.FilesService
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
@Table(name = "question")
class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int?,

    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "week_id")
    val relatedWeek : Week,

    @Column(name = "type", nullable = false)
    val type : String,

    @Column(name = "question_description", nullable = false)
    val questionDescription : String,

    @Column(name = "phase", nullable = false)
    val phase : Int,

    @OneToOne(cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "question_function_id", nullable = true)
    val function : QuestionFunction?,

    @Column(name = "punishment", nullable = true)
    val punishment : Int?,

    @Column(name="question_picture", nullable = true)
    val questionPicture: String?,

    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "game_id")
    val relatedGame: Game

) {
    fun toQuestionDto(filesService: FilesService): QuestionDto {
        return this.let {
            QuestionDto(
                it.id,
                it.relatedWeek.id!!,
                it.type,
                it.questionDescription,
                it.phase,
                it.function?.toQuestionFunctionDto(),
                it.punishment,
                it.questionPicture,
                it.relatedGame.id!!,
                it.questionPicture?.let { picture -> filesService.generatePresignedUrl("schmell-files", picture) }
            )
        }
    }
}