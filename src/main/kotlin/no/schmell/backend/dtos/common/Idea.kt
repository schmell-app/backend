package no.schmell.backend.dtos.common

import no.schmell.backend.dtos.auth.SimpleUserDto
import no.schmell.backend.lib.enums.IdeaCategory

data class CreateIdeaDto(
    val ideaText: String,
    val category: IdeaCategory,
    val createdBy: Int
)
data class IdeaDto(
    val id : Int?,
    val ideaText : String,
    val category : IdeaCategory,
    val createdBy : SimpleUserDto,
)
data class IdeaFilters(
    val category: IdeaCategory?,
    val createdBy: Int?
)