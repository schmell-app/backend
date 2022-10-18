package no.schmell.backend.dtos.common

import no.schmell.backend.dtos.auth.UserDto
import no.schmell.backend.lib.enums.IdeaCategory

data class CreateIdeaParams(
    val id : Int?,
    val ideaText : String,
    val category : IdeaCategory,
    val createdBy : Int,
) {
    fun fromCreateToDto(createdBy: UserDto): IdeaDto {
        return this.let {
            IdeaDto(
                it.id ?: null,
                it.ideaText,
                it.category,
                createdBy
            )
        }
    }
}
