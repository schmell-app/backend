package no.schmell.backend.entities.cms

import no.schmell.backend.dtos.cms.QuestionTypeDto
import javax.persistence.*

@Entity
@Table(name = "question_type")
class QuestionType (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int?,

    @Column(name = "name", nullable = false)
    val name : String,

    @Column(name = "hex_color", nullable = false)
    val hexColor : String,

    @Column(name = "hint", nullable = false, length = 2000)
    val hint : String,
) {
    fun toQuestionTypeDto(): QuestionTypeDto {
        return this.let {
            QuestionTypeDto(
                it.id,
                it.name,
                it.hexColor,
                it.hint
            )
        }
    }
}