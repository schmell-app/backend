package no.schmell.backend.services.cms

import mu.KLogging
import no.schmell.backend.dtos.cms.*
import no.schmell.backend.entities.cms.Question
import no.schmell.backend.entities.cms.QuestionFunction
import no.schmell.backend.entities.common.GameSession
import no.schmell.backend.lib.defaults.defaultActiveWeeks
import no.schmell.backend.lib.enums.GroupSize
import no.schmell.backend.repositories.cms.GameRepository
import no.schmell.backend.repositories.cms.QuestionFunctionRepository
import no.schmell.backend.repositories.cms.QuestionRepository
import no.schmell.backend.repositories.common.GameSessionRepository
import no.schmell.backend.services.files.FilesService
import no.schmell.backend.utils.switchOutQuestionStringWithPlayers
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class QuestionsService(
    private val questionRepository: QuestionRepository,
    val questionFunctionRepository: QuestionFunctionRepository,
    val gameRepository: GameRepository,
    val filesService: FilesService,
    val gamesService: GamesService,
    val questionTypeService: QuestionTypeService,
    val gameSessionRepository: GameSessionRepository,
) {

    companion object : KLogging()

    fun getAll(filter: QuestionFilter): QuestionPaginatedResponse {
        val questions = questionRepository.findAllByFilters(
                filter.relatedGame,
                filter.questionType,
                filter.questionSearch,
                filter.weekNumbers,
                PageRequest.of(filter.page - 1, filter.pageSize)
        )
        val total = questionRepository.countAllByFilters(
                filter.relatedGame,
                filter.questionType,
                filter.questionSearch,
                filter.weekNumbers
        )

        return QuestionPaginatedResponse(
            questions = questions.map { it.toQuestionDto(filesService) },
            total = total,
            page = filter.page,
            lastPage = (total / filter.pageSize) + 1
        )
    }

    fun createSeveral(dto: List<CreateQuestionDto>): List<QuestionDto> {
        val savedQuestions = questionRepository.saveAll(dto.map {
            val relatedGame = gameRepository.findByIdOrNull(it.relatedGame) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
            val relatedQuestionType = questionTypeService.getById(it.relatedQuestionType)
            var relatedFunction: QuestionFunction? = null

            it.function?.let { questionFunction -> relatedFunction = questionFunctionRepository.save(QuestionFunction(
                null,
                questionFunction.timer,
                questionFunction.answer,
                questionFunction.challenges,
                questionFunction.questions,
                questionFunction.options
            )) }
            Question(
                null,
                it.activeWeeks?.joinToString(",") ?: defaultActiveWeeks,
                it.questionDescription,
                it.phase,
                relatedFunction,
                it.punishment,
                null,
                relatedGame,
                relatedQuestionType,
                it.groupSize ?: GroupSize.All
            )
        })

        gamesService.update(savedQuestions.first().relatedGame.id!!,
            UpdateGameDto(null, null, null))
        return savedQuestions.map { question -> question.toQuestionDto(filesService) }
    }

    fun getById(id: Int): QuestionDto {
        val question = questionRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return question.toQuestionDto(filesService)
    }

    fun create(dto: CreateQuestionDto): QuestionDto {
        val relatedGame = gameRepository.findByIdOrNull(dto.relatedGame) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val relatedQuestionType = questionTypeService.getById(dto.relatedQuestionType)

        gamesService.update(relatedGame.id!!, UpdateGameDto(null, null, null))

        var relatedFunction: QuestionFunction? = null

        dto.function?.let {
            relatedFunction = questionFunctionRepository.save(QuestionFunction(
            null,
            it.timer,
            it.answer,
            it.challenges,
            it.questions,
            it.options
        )) }

        return questionRepository.save(Question(
            null,
            dto.activeWeeks?.joinToString(",") ?: defaultActiveWeeks,
            dto.questionDescription,
            dto.phase,
            relatedFunction,
            dto.punishment,
            null,
            relatedGame,
            relatedQuestionType,
            dto.groupSize ?: GroupSize.All
        )).toQuestionDto(filesService)
    }

    fun update(id: Int, dto: UpdateQuestionDto): QuestionDto {
        val questionToUpdate = questionRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        val questionType = if (dto.relatedQuestionType != questionToUpdate.questionType.id)
            questionTypeService.getById(dto.relatedQuestionType)
        else questionToUpdate.questionType

        var relatedFunction: QuestionFunction? = null
        if (questionToUpdate.function == null) {
            relatedFunction = questionFunctionRepository.save(QuestionFunction(
                null,
                dto.function?.timer,
                dto.function?.answer,
                dto.function?.challenges,
                dto.function?.questions,
                dto.function?.options
            ))
        }

        if (questionToUpdate.function != null && dto.function != null) {
            val updatedFunction = QuestionFunction(
                questionToUpdate.function.id,
                dto.function.timer ?: questionToUpdate.function.timer,
                dto.function.answer ?: questionToUpdate.function.answer,
                dto.function.challenges ?: questionToUpdate.function.challenges,
                dto.function.questions ?: questionToUpdate.function.questions,
                dto.function.options ?: questionToUpdate.function.options
            )
            relatedFunction = questionFunctionRepository.save(updatedFunction)
        }

        val updatedQuestion = Question(
            questionToUpdate.id,
            dto.activeWeeks?.joinToString(",") ?: questionToUpdate.activeWeeks,
            dto.questionDescription ?: questionToUpdate.questionDescription,
            dto.phase ?: questionToUpdate.phase,
            relatedFunction ?: questionToUpdate.function,
            dto.punishment ?: questionToUpdate.punishment,
            questionToUpdate.questionPicture,
            questionToUpdate.relatedGame,
            questionType,
            dto.groupSize ?: questionToUpdate.groupSize
        )

        gamesService.update(questionToUpdate.relatedGame.id!!, UpdateGameDto(null, null, null))
        return questionRepository.save(updatedQuestion).toQuestionDto(filesService)
    }

    private fun addPlayersToQuestions(players: List<String>, questions: List<QuestionDto>): List<QuestionDto> {
        val updatedQuestions : MutableList<QuestionDto> = mutableListOf()

        try {
            for (question in questions) {
                val updatedQuestionDescription: String =
                    switchOutQuestionStringWithPlayers(question.questionDescription, players)
                updatedQuestions.add(
                    QuestionDto(
                        question.id,
                        question.activeWeeks,
                        updatedQuestionDescription,
                        question.phase,
                        question.function,
                        question.punishment,
                        question.questionPicture,
                        question.relatedGame,
                        question.questionPicture?.let { file -> filesService.generatePresignedUrl("schmell-files", file)},
                        question.questionType,
                        question.groupSize
                    )
                )
            }
        } catch (e: Exception) {
            logger.info { "Something, went wrong when adding player, error message: ${e.message}" }
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }

        return updatedQuestions
    }

    fun addPlayerToQuestionsInGame(
        players: List<String>,
        currentIndex: Int,
        uneditedQuestions: List<QuestionDto>,
        editedQuestions: List<QuestionDto>
    ): List<QuestionDto> {
        val firstPartOfQuestions = editedQuestions.subList(0, currentIndex + 1)
        logger.info { firstPartOfQuestions }
        val secondPartOfQuestions = uneditedQuestions.subList(currentIndex + 1, editedQuestions.size)
        logger.info { secondPartOfQuestions }

        val editedSecondPartOfQuestions = addPlayersToQuestions(players, secondPartOfQuestions)

        return firstPartOfQuestions + editedSecondPartOfQuestions
    }

    fun delete(id: Int) {
        return if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun addQuestionPicture(id: Int, file: MultipartFile): QuestionDto {
        val question = questionRepository.findByIdOrNull(id)

        return if (question != null) {
            val uploadedFile = filesService.saveFile(file, "schmell-files", "questionPictures")

            questionRepository.save(Question(
                question.id,
                question.activeWeeks,
                question.questionDescription,
                question.phase,
                question.function,
                question.punishment,
                uploadedFile?.fileName,
                question.relatedGame,
                question.questionType,
                question.groupSize
            )).toQuestionDto(filesService)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun startGame(dto: GamePlayParams): GamePlayResponse {
        var questionsToPlay = this.questionRepository.findAllByRelatedGameId(dto.relatedGame);

        val weekNumbers = listOf(dto.weekNumber);
        questionsToPlay = questionsToPlay.filter { question ->
            weekNumbers.any { it ->
                val activeWeeksForQuestions = question.activeWeeks?.split(",")?.map { it.toInt() }
                activeWeeksForQuestions?.contains(it) ?: false
            }
        }
        questionsToPlay = questionsToPlay.shuffled()
        questionsToPlay = questionsToPlay.sortedBy { it.phase }

        val questions = questionsToPlay.map { question -> question.toQuestionDto(filesService) }

        val questionsWithPlayers = this.addPlayersToQuestions(dto.players, questions).filter { question ->
            if (dto.players.size < 9) question.groupSize == GroupSize.S || question.groupSize == GroupSize.All
            else if (dto.players.size < 17) question.groupSize == GroupSize.M || question.groupSize == GroupSize.All
            else question.groupSize == GroupSize.L || question.groupSize == GroupSize.All
        }
        val relatedGame = gameRepository.findById(dto.relatedGame).orElse(null)

        gameSessionRepository.save(GameSession(
            null,
            LocalDateTime.now(),
            relatedGame,
        ))

        return GamePlayResponse(
            questions,
            questionsWithPlayers
        )
    }
}