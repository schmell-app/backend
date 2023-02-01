package no.schmell.backend.entities.cms

import no.schmell.backend.dtos.cms.question.QuestionFunctionDto
import javax.persistence.*

@Entity
@Table(name = "question_function")
class QuestionFunction(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int?,

    @Column(name = "timer", nullable = true)
    val timer : Int?,

    @Column(name = "answer", nullable = true)
    val answer : String?,

    @Column(name = "challenges", nullable = true)
    @ElementCollection
    val challenges : List<String>?,

    @Column(name = "questions", nullable = true)
    @ElementCollection
    val questions : List<String>?,

    @Column(name = "options", nullable = true)
    @ElementCollection
    val options : List<String>?,
) {
    fun toQuestionFunctionDto(): QuestionFunctionDto {
        return this.let {
            QuestionFunctionDto(
                it.id,
                it.timer,
                it.answer,
                it.challenges,
                it.questions,
                it.options
            )
        }
    }
}