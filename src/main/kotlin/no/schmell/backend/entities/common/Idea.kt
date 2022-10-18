package no.schmell.backend.entities.common

import no.schmell.backend.dtos.common.IdeaDto
import no.schmell.backend.entities.auth.User
import no.schmell.backend.lib.enums.IdeaCategory
import javax.persistence.*

@Entity
@Table(name = "idea")
class Idea(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,

    @Column(name = "idea_text", nullable = false)
    val ideaText: String,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: IdeaCategory,

    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "application_user_id")
    val createdBy: User,
) {
    fun toIdeaDto(gcpProjectId: String, gcpBucketId: String, gcpConfigFile: String): IdeaDto {
        return this.let {
            IdeaDto(
                it.id,
                it.ideaText,
                it.category,
                it.createdBy.toUserDto(gcpProjectId, gcpBucketId, gcpConfigFile)
            )
        }
    }
}

