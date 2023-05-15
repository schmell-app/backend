package no.schmell.backend.dtos.common

import no.schmell.backend.dtos.cms.SimpleGameDto
import no.schmell.backend.lib.enums.*

data class StatisticsGeneralResponse(
    val dayStatistics: DayStatistics,
    val gameCount: GameCount,
    val questionsCount: QuestionsCount,
    val taskCount: TaskCount
)
data class StatisticsOverviewResponse(
    val usersCount: UsersCount,
    val gamePopularity: List<GamePopularityDto>,
    val marketing: List<MarketingDto>,
    val usageTimes: List<UsageTimesDto>,
)

data class GamePopularityDto(
    val game: SimpleGameDto,
    val count: Int,
)

data class MarketingDto(
    val provider: MarketingProviders,
    val count: Int,
)

data class UsageTimesDto(
    val time: String,
    val count: Int,
)

data class UsersCount(
    val view: StatisticsView,
    val grouping: StatisticsGroup,
    val count: List<UsersCountDto>
)

data class UsersCountDto(
    val countStamp: Int,
    val count: Int,
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
    val usersView: StatisticsView,
    val usersGroup: StatisticsGroup,
    val popularity: StatisticsView,
    val usage : StatisticsView,
)