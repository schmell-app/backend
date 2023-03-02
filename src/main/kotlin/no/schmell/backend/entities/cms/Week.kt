package no.schmell.backend.entities.cms

import no.schmell.backend.dtos.cms.week.WeekDto
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
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

    @OneToMany(mappedBy = "relatedWeek", cascade = [CascadeType.REMOVE])
    val questions: List<Question>?
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



