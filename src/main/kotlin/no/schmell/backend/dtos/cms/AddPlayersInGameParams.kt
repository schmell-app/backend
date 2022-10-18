package no.schmell.backend.dtos.cms

data class AddPlayersInGameParams(
    val players: List<String>,
    val currentIndex: Int,
    val uneditedQuestions: List<QuestionListDto>,
    val editedQuestions: List<QuestionListDto>,
)
