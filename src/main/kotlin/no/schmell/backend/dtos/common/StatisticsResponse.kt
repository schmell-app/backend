package no.schmell.backend.dtos.common

import no.schmell.backend.lib.enums.TaskCategory

data class StatisticsResponse(
    val userCount: UserCount,
    val gameCount: GameCount,
    val questionsCount: QuestionsCount,
    val taskCount: TaskCount
)

data class UserCount(
    val count: Long,
)

data class GameCount(
    val count: Int,
)

data class QuestionsCount(
    val totalCount: Int,
    val countByGame: List<CountByGame>
)

data class TaskCount(
    val unsolved: Int,
    val overdue: Int,
    val countByCategory: List<CountByCategory>
)

data class CountByCategory(
    val category: TaskCategory,
    val count: Int,
)

data class CountByGame(
    val gameId: Int,
    val gameName: String,
    val count: Int,
)