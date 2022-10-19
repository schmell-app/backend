package no.schmell.backend.entities.cms

import no.schmell.backend.dtos.cms.WeekDto
import javax.persistence.*

@Entity
@Table(name = "week")
class Week(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int?,

    @ManyToOne(cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "game_id")
    val relatedGame : Game,

    @Column(name = "week_number", nullable = false)
    val weekNumber: Int,
) {
    fun toWeekDto(): WeekDto {
        return this.let {
            WeekDto(
                it.id,
                it.relatedGame.toGameDto(),
                it.weekNumber
            )
        }
    }
}



