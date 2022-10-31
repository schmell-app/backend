package no.schmell.backend.entities.cms

import no.schmell.backend.dtos.cms.QuestionDto
import no.schmell.backend.dtos.cms.QuestionListDto
import no.schmell.backend.services.files.FilesService
import javax.persistence.*

@Entity
@Table(name = "question")
class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int?,

    @ManyToOne(cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "week_id")
    val relatedWeek : Week,

    @Column(name = "type", nullable = false)
    val type : String,

    @Column(name = "question_description", nullable = false)
    val questionDescription : String,

    @Column(name = "phase", nullable = false)
    val phase : Int,

    @Column(name = "function", nullable = true)
    val function : String?,

    @Column(name = "punishment", nullable = false)
    val punishment : Int,

    @Column(name="question_picture", nullable = true)
    val questionPicture: String?,

    @ManyToOne(cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "game_id")
    val relatedGame: Game

) {
    fun toQuestionDto(filesService: FilesService): QuestionDto {
        return this.let {
            QuestionDto(
                it.id,
                it.relatedWeek.toWeekDto(filesService),
                it.type,
                it.questionDescription,
                it.phase,
                it.function,
                it.punishment,
                it.questionPicture,
                it.relatedGame.toGameDto(filesService),
                it.questionPicture?.let { picture -> filesService.generatePresignedUrl("schmell-files", picture) }
            )
        }
    }

    fun toQuestionListDto(filesService: FilesService): QuestionListDto {
        return this.let {
            QuestionListDto(
                it.id,
                it.relatedWeek.id,
                it.type,
                it.questionDescription,
                it.phase,
                it.function,
                it.punishment,
                it.questionPicture,
                it.relatedGame.id,
                it.questionPicture?.let { picture -> filesService.generatePresignedUrl("schmell-files", picture) }
            )
        }
    }
}