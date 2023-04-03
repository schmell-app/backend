package no.schmell.backend.dtos.cms.question

data class PlayQuestionParams(
    val relatedWeek: Int,
    val players: List<String>
)
