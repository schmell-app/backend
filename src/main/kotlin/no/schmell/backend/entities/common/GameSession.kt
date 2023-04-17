package no.schmell.backend.entities.common

import no.schmell.backend.dtos.common.GameSessionDto
import no.schmell.backend.entities.cms.Game
import no.schmell.backend.services.files.FilesService
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "game_session")
class GameSession (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int?,

    @Column(name = "created_at", nullable = false)
    val createdAt : LocalDateTime,

    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "game_id")
    val relatedGame: Game
) {
    fun toGameSessionDto(filesService: FilesService): GameSessionDto {
        return GameSessionDto(
            this.id,
            this.createdAt,
            this.relatedGame.toGameDto(filesService)
        )
    }
}