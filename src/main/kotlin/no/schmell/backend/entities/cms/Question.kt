package no.schmell.backend.entities.cms

import no.schmell.backend.dtos.cms.QuestionDto
import no.schmell.backend.dtos.cms.QuestionListDto
import no.schmell.backend.lib.getSignedUrl
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
    fun toQuestionDto(gcpProjectId: String, gcpBucketId: String, gcpConfigFile: String): QuestionDto {
        return this.let {
            QuestionDto(
                it.id,
                it.relatedWeek.toWeekDto(gcpProjectId, gcpBucketId, gcpConfigFile),
                it.type,
                it.questionDescription,
                it.phase,
                it.function,
                it.punishment,
                it.questionPicture,
                getSignedUrl(gcpProjectId, gcpBucketId, gcpConfigFile, it.questionPicture),
                it.relatedGame.toGameDto(gcpProjectId, gcpBucketId, gcpConfigFile)
            )
        }
    }

    fun toQuestionListDto(gcpProjectId: String, gcpBucketId: String, gcpConfigFile: String): QuestionListDto {
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
                getSignedUrl(gcpProjectId, gcpBucketId, gcpConfigFile, it.questionPicture),
                it.relatedGame.id
            )
        }
    }
}