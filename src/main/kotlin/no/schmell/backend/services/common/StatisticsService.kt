package no.schmell.backend.services.common

import no.schmell.backend.dtos.common.*
import no.schmell.backend.entities.common.GameSession
import no.schmell.backend.lib.enums.StatisticsUserView
import no.schmell.backend.lib.enums.TaskCategory
import no.schmell.backend.lib.enums.TaskStatus
import no.schmell.backend.repositories.cms.GameRepository
import no.schmell.backend.repositories.cms.QuestionRepository
import no.schmell.backend.repositories.common.GameSessionRepository
import no.schmell.backend.repositories.crm.TaskRepository
import no.schmell.backend.utils.*
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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
                tasks.count { task -> task.category == category }
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

    fun getOverviewStatistics(dto: StatisticsFilter): StatisticsOverviewResponse {
        val (usersView, popularityView, incomeView) = dto
        val (firstDayOfWeek, lastDayOfWeek) = getFirstAndLastDayOfWeek()
        val (firstDayOfMonth, lastDayOfMonth) = getFirstAndLastDayOfMonth()
        val (firstDayOfYear, lastDayOfYear) = getFirstAndLastDayOfYear()

        val (usersDateFrom, usersDateTo) = if (usersView == StatisticsUserView.MonthDay || usersView == StatisticsUserView.MonthWeek)
            Pair(firstDayOfMonth, lastDayOfMonth) else Pair(firstDayOfYear, lastDayOfYear)

        val userSessions = this.gameSessionRepository.findAllInDateRange(usersDateFrom, usersDateTo)
        val groupedUserSessions = this.groupUserSessions(userSessions, usersView)




    }

    private fun groupUserSessions(sessions: List<GameSession>, view: StatisticsUserView): List<Pair<Int, Int>> {
        val userSessionsGrouped: List<Pair<Int, Int>> = listOf()
        var sortedSessions = sessions.sortedBy { it.createdAt }

        if (view == StatisticsUserView.MonthWeek) {
            val numberOfWeeksInMonth = getWeeksInMonth()
        } else if (view == StatisticsUserView.MonthDay) {
            val numberOfDaysInMonth = getDaysInMonth()
        } else {

        }

        return userSessionsGrouped
    }

}