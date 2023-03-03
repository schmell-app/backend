package no.schmell.backend.services.common

import no.schmell.backend.dtos.common.*
import no.schmell.backend.lib.enums.TaskCategory
import no.schmell.backend.lib.enums.TaskStatus
import no.schmell.backend.repositories.auth.UserRepository
import no.schmell.backend.repositories.cms.GameRepository
import no.schmell.backend.repositories.cms.QuestionRepository
import no.schmell.backend.repositories.crm.TaskRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class StatisticsService(
    val taskRepository: TaskRepository,
    val gameRepository: GameRepository,
    val questionRepository: QuestionRepository,
    val userRepository: UserRepository,
) {

    fun getStatistics(): StatisticsResponse {
        val games = gameRepository.findAll()
        val questions = questionRepository.findAll()
        val tasks = taskRepository.findAll()

        val questionsByGame: List<CountByGame> = games.map { game ->
            CountByGame(
                game.id!!,
                game.name,
                questions.count { question -> question.relatedGame == game }
            )
        }

        val countByCategory: List<CountByCategory> = TaskCategory.values().map { category ->
            CountByCategory(
                category,
                tasks.count { task -> task.category == category }
            )
        }

        return StatisticsResponse(
            userCount = UserCount(
                count = userRepository.count()
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

}