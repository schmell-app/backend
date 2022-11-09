package no.schmell.backend.dtos.cms.question

data class AddPlayersParams(
    val players: List<String>,
    val questions: List<QuestionDto>
)
