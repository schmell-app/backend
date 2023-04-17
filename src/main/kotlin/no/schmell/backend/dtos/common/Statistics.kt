package no.schmell.backend.dtos.common

import no.schmell.backend.dtos.cms.SimpleGameDto
import no.schmell.backend.lib.enums.MarketingProviders
import no.schmell.backend.lib.enums.StatisticsUserView
import no.schmell.backend.lib.enums.StatisticsView
import no.schmell.backend.lib.enums.TaskCategory

data class StatisticsGeneralResponse(
    val dayStatistics: DayStatistics,
    val gameCount: GameCount,
    val questionsCount: QuestionsCount,
    val taskCount: TaskCount
)
data class StatisticsOverviewResponse(
    val usersCount: UsersCount,
    val gamePopularity: List<Pair<SimpleGameDto, Int>>,
    val gameIncome: List<Pair<SimpleGameDto, Int>>,
    val usageTimes: List<Pair<String, Int>>,
    val marketing: List<Pair<MarketingProviders, Int>>
)

data class UsersCount(
    val view: StatisticsUserView,
    val count: List<Pair<Int, Int>>
)

data class DayStatistics(
    val userCount: Int,
    val gamesPlayed: Int
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
    val amountOfSessions: Int,
)

data class StatisticsFilter(
    val users : StatisticsUserView,
    val popularity: StatisticsView,
    val income : StatisticsView,
)