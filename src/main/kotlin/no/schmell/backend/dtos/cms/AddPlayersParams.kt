package no.schmell.backend.dtos.cms

data class AddPlayersParams(
    val players: List<String>,
    val questions: List<QuestionListDto>
)
