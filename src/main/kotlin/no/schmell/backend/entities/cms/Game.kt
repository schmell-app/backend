package no.schmell.backend.entities.cms

import no.schmell.backend.dtos.cms.game.GameDto
import no.schmell.backend.dtos.cms.game.SimpleGameDto
import no.schmell.backend.lib.enums.GameStatus
import no.schmell.backend.services.files.FilesService
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

    @OneToMany(mappedBy = "relatedGame", cascade = [CascadeType.REMOVE])
    val weeks: List<Week>?,

    @OneToMany(mappedBy = "relatedGame", cascade = [CascadeType.REMOVE])
    val questions: List<Question>?,

    @Column(name ="is_family_friendly", nullable = false, columnDefinition = "boolean default false")
    val isFamilyFriendly: Boolean
){
    fun toGameDto(filesService: FilesService): GameDto {
        return this.let {
            GameDto(
                it.id,
                it.name,
                it.description,
                it.lastUpdated,
                it.status,
                it.logo,
                it.logo?.let { logo -> filesService.generatePresignedUrl("schmell-files", logo) },
                it.isFamilyFriendly
            )
        }
    }

    fun toSimpleGameDto(): SimpleGameDto {
        return this.let {
            SimpleGameDto(
                it.id,
                it.name
            )
        }
    }
}