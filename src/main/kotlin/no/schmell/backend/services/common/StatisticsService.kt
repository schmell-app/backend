package no.schmell.backend.services.common

import no.schmell.backend.dtos.cms.SimpleGameDto
import no.schmell.backend.dtos.common.*
import no.schmell.backend.entities.common.GameSession
import no.schmell.backend.lib.enums.*
import no.schmell.backend.repositories.cms.GameRepository
import no.schmell.backend.repositories.cms.QuestionRepository
import no.schmell.backend.repositories.common.GameSessionRepository
import no.schmell.backend.repositories.crm.TaskRepository
import no.schmell.backend.utils.*
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.WeekFields
import java.util.*

@Service
class StatisticsService(
    val taskRepository: TaskRepository,
    val gameRepository: GameRepository,
    val questionRepository: QuestionRepository,
    val gameSessionRepository: GameSessionRepository,
) {

    fun getGeneralStatistics(): StatisticsGeneralResponse {
        val games = gameRepository.findAll()
        val questions = questionRepository.findAll()
        val tasks = taskRepository.findAll()
        val (firstDayOfWeek, lastDayOfWeek) = getFirstAndLastDayOfWeek()
        val today = LocalDate.now()

        val questionsByGame: List<CountByGame> = games.map { game ->
            CountByGame(
                game.id!!,
                game.name,
                questions.count { question -> question.relatedGame == game },
                gameSessionRepository.countAllInDateRangeWithGame(
                    firstDayOfWeek,
                    lastDayOfWeek,
                    game
                )
            )
        }

        val countByCategory: List<CountByCategory> = TaskCategory.values().map { category ->
            CountByCategory(
                category,
                tasks.filter { task -> task.status != TaskStatus.DONE }.count { task -> task.category == category }
            )
        }

        return StatisticsGeneralResponse(
            dayStatistics = DayStatistics(
                userCount = gameSessionRepository.countAllInDateRange(
                    today.atStartOfDay(),
                    today.atTime(LocalTime.MAX)
                ),
                gamesPlayed = gameSessionRepository.countUniqueGamesInDateRange(
                    today.atStartOfDay(),
                    today.atTime(LocalTime.MAX)
                )
            ),
            gameCount = GameCount(
                count = games.count(),
            ),
            questionsCount = QuestionsCount(
                totalCount = questions.count(),
                questionsByGame,
            ),
            taskCount = TaskCount(
                unsolved = tasks.filter { task -> task.status != TaskStatus.DONE }.size,
                overdue = tasks
                    .filter { task -> task.status != TaskStatus.DONE }
                    .count { task -> task.deadline <= LocalDateTime.now() },
                countByCategory,
            )
        )
    }

    fun getStatisticsOverview(filters: StatisticsFilter): StatisticsOverviewResponse {
        val (usersView, usersGroup, popularityView, usageView) = filters

        val (usersDateFrom, usersDateTo) = getRange(usersView)
        val (popularityDateFrom, popularityDateTo) = getRange(popularityView)
        val (usageDateFrom, usageDateTo) = getRange(usageView)

        val userSessions = this.gameSessionRepository.findAllInDateRange(usersDateFrom, usersDateTo)
        val gameSessionsForPopularity = this.gameSessionRepository.findAllInDateRange(popularityDateFrom, popularityDateTo)
        val gameSessionsForUsage = this.gameSessionRepository.findAllInDateRange(usageDateFrom, usageDateTo)

        val groupedUserSessions = this.groupUserSessions(userSessions, usersGroup)
        val popularity = this.getPopularity(gameSessionsForPopularity)
        val usage = this.getUsageTimes(gameSessionsForUsage)


        return StatisticsOverviewResponse(
            UsersCount(
                view = usersView,
                grouping = usersGroup,
                count = groupedUserSessions
            ),
            gamePopularity = popularity,
            usageTimes = usage,
            marketing = getMarketingStatistics()
        )
    }

    private fun groupUserSessions(sessions: List<GameSession>, group: StatisticsGroup): List<UsersCountDto> {
        val sortedSessions = sessions.sortedBy { it.createdAt }
        val userSessionsGrouped = mutableListOf<UsersCountDto>()

        when (group) {
            StatisticsGroup.Week -> {
                val numberOfWeeksInMonth = getWeeksInMonth()
                for (i in 1..numberOfWeeksInMonth) {
                    val sessionsInWeek = sortedSessions.filter { session ->
                        val weekOfYear = session.createdAt.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())
                        weekOfYear == i
                    }
                    userSessionsGrouped.add(UsersCountDto(i, sessionsInWeek.size))
                }
            }
            StatisticsGroup.Day -> {
                val numberOfDaysInMonth = getDaysInMonth()
                for (i in 1..numberOfDaysInMonth) {
                    val sessionsInDay = sortedSessions.filter { session ->
                        val dayOfMonth = session.createdAt.dayOfMonth
                        dayOfMonth == i
                    }
                    userSessionsGrouped.add(UsersCountDto(i, sessionsInDay.size))
                }
            }
            StatisticsGroup.Month -> {
                val monthsInYear = NUMBER_OF_MONTHS
                for (i in 1..monthsInYear) {
                    val sessionsInMonth = sortedSessions.filter { session ->
                        val monthOfYear = session.createdAt.monthValue
                        monthOfYear == i
                    }
                    userSessionsGrouped.add(UsersCountDto(i, sessionsInMonth.size))
                }
            }
        }

        return userSessionsGrouped
    }

    private fun getPopularity(gameSessions: List<GameSession>): List<GamePopularityDto> {
        val games = gameRepository.findAllDeployedGames(GameStatus.DEPLOYED)
        val totalCount = gameSessions.size.toDouble()

        return games.map { game ->
            val sessionsForGameCount = gameSessions.count { it.relatedGame.id == game.id }
            val percentage = ((sessionsForGameCount / totalCount) * 100).toInt()
            val simpleGame = SimpleGameDto(game.id, game.name)

            GamePopularityDto(simpleGame, percentage)
        }
    }

    private fun getUsageTimes(gameSessions: List<GameSession>): List<UsageTimesDto> {
        val usageTimes = mutableListOf<UsageTimesDto>()

        for (interval in TIME_INTERVALS) {
            val (start, end) = interval.split("-").map { it.toInt() }
            val sessionsInInterval = gameSessions.count { session ->
                val hour = session.createdAt.hour
                hour in start until end
            }
            usageTimes.add(UsageTimesDto(interval, sessionsInInterval))
        }

        return usageTimes
    }

    //TODO: Connect with respective marketing providers
    private fun getMarketingStatistics(): List<MarketingDto> {
        val tiktokStatistics = MarketingDto(
            MarketingProviders.TikTok,
            0
        )
        val instagramStatistics = MarketingDto(
            MarketingProviders.Instagram,
            0
        )
        val snapchatStatistics = MarketingDto(
            MarketingProviders.SnapChat,
            0
        )
        val referralStatistics = MarketingDto(
            MarketingProviders.Referral,
            0
        )
        val appStoreStatistics = MarketingDto(
            MarketingProviders.AppStore,
            0
        )
        val googlePlayStatistics = MarketingDto(
            MarketingProviders.GooglePlay,
            0
        )

        return listOf(tiktokStatistics, instagramStatistics, snapchatStatistics, referralStatistics, appStoreStatistics, googlePlayStatistics)
    }

}