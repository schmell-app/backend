package no.schmell.backend.dtos.cms

data class CreateWeekParams(
    val id: Int?,
    val relatedGame: Int,
    val weekNumber: Int,
) {
    fun fromCreateToDto(game: GameDto): WeekDto {
        return this.let {
            WeekDto(
                it.id ?: null,
                game,
                it.weekNumber
            )
        }
    }
}
