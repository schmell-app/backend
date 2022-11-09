package no.schmell.backend.dtos.cms.question

data class AddPlayersInGameParams(
    val players: List<String>,
    val currentIndex: Int,
    val uneditedQuestions: List<QuestionDto>,
    val editedQuestions: List<QuestionDto>,
)
