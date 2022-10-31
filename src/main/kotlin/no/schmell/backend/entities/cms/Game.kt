package no.schmell.backend.entities.cms

import no.schmell.backend.dtos.cms.GameDto
import no.schmell.backend.lib.enums.GameStatus
import no.schmell.backend.lib.files.GenerateObjectSignedUrl
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "game")
class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "description", nullable = false)
    val description: String,

    @Column(name = "last_updated", nullable = false)
    val lastUpdated: LocalDateTime,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: GameStatus,

    @Column(name="logo", nullable = true)
    val logo: String?,

    @Column(name="release_date", nullable = true)
    val releaseDate: LocalDate?
){
    fun toGameDto(generateObjectSignedUrl: GenerateObjectSignedUrl): GameDto {

        return this.let {
            GameDto(
                it.id,
                it.name,
                it.description,
                it.lastUpdated,
                it.status,
                it.logo,
                it.releaseDate,
                generateObjectSignedUrl.generateSignedUrl(it.logo),
            )
        }
    }
}