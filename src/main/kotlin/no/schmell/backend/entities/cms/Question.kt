package no.schmell.backend.entities.cms

import no.schmell.backend.dtos.cms.QuestionDto
import no.schmell.backend.lib.defaults.defaultActiveWeeksSql
import no.schmell.backend.lib.enums.GroupSize
import no.schmell.backend.services.files.FilesService
import javax.persistence.*

@Entity
@Table(name = "question")
class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int?,

    @Column(name = "active_weeks", nullable = false,
        columnDefinition = "varchar(255) default $defaultActiveWeeksSql"
    )
    val activeWeeks : String?,

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
    val relatedGame: Game,

    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "question_type_id")
    val questionType: QuestionType,

    @Column(name = "group_size", nullable = false, columnDefinition = "varchar(255) default 'All'")
    @Enumerated(EnumType.STRING)
    val groupSize: GroupSize,

    @Column(name = "dislikes_count", nullable = false, columnDefinition = "int default 0")
    val dislikesCount: Int,
) {
    fun toQuestionDto(filesService: FilesService): QuestionDto {
        return this.let {
            QuestionDto(
                it.id,
                it.activeWeeks?.split(",")?.map { weekString -> weekString.toInt() },
                it.questionDescription,
                it.phase,
                it.function?.toQuestionFunctionDto(),
                it.punishment,
                it.questionPicture,
                it.relatedGame.id!!,
                it.questionPicture?.let { picture -> filesService.generatePresignedUrl("schmell-files", picture) },
                it.questionType.toQuestionTypeDto(),
                it.groupSize,
                it.dislikesCount
            )
        }
    }
}