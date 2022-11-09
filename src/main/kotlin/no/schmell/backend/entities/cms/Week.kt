package no.schmell.backend.entities.cms

import no.schmell.backend.dtos.cms.week.WeekDto
import javax.persistence.*

@Entity
@Table(name = "week")
class Week(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int?,

    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "game_id")
    val relatedGame : Game,

    @Column(name = "week_number", nullable = false)
    val weekNumber: Int,
) {
    fun toWeekDto(): WeekDto {
        return this.let {
            WeekDto(
                it.id,
                it.relatedGame.id ?: 0, //Should never be null
                it.weekNumber
            )
        }
    }
}



