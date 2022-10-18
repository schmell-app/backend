package no.schmell.backend.dtos.cms

data class CreateQuestionParams(
    val id: Int?,
    val relatedWeek: Int,
    val type: String,
    val questionDescription: String,
    val phase: Int,
    val function: String?,
    val punishment: Int,
    val relatedGame: Int,
) {
    fun fromCreateToDto(weekDto: WeekDto, gameDto: GameDto): QuestionDto {
        return this.let {
            QuestionDto(
                it.id ?: null,
                weekDto,
                it.type,
                it.questionDescription,
                it.phase,
                it.function,
                it.punishment,
                null,
                null,
                gameDto
            )
        }
    }
}
