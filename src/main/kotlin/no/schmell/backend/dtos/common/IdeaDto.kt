package no.schmell.backend.dtos.common

import no.schmell.backend.dtos.auth.UserDto
import no.schmell.backend.entities.common.Idea
import no.schmell.backend.lib.enums.IdeaCategory

data class IdeaDto(
    val id : Int?,
    val ideaText : String,
    val category : IdeaCategory,
    val createdBy : UserDto,
) {
    fun toIdeaEntity(): Idea {
        return this.let {
            Idea(
                it.id ?: null,
                it.ideaText,
                it.category,
                it.createdBy.toUserEntity()
            )
        }
    }
}