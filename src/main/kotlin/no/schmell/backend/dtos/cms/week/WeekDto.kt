package no.schmell.backend.dtos.cms.week

data class WeekDto(
    val id : Int?,
    var relatedGame : Int,
    val weekNumber: Int,
)

data class CreateWeekDto(
    var relatedGame : Int,
    val weekNumber: Int,
)