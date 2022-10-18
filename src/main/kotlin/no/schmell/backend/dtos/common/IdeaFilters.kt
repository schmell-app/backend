package no.schmell.backend.dtos.common

import no.schmell.backend.lib.enums.IdeaCategory

data class IdeaFilters(
    val category: IdeaCategory?,
    val createdBy: Int?
)
