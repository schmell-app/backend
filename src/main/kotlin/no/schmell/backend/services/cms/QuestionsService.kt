package no.schmell.backend.services.cms

import mu.KLogging
import no.schmell.backend.dtos.cms.game.UpdateGameDto
import no.schmell.backend.dtos.cms.question.CreateQuestionDto
import no.schmell.backend.repositories.cms.QuestionRepository
import no.schmell.backend.utils.switchOutQuestionStringWithPlayers
import no.schmell.backend.dtos.cms.question.QuestionDto
import no.schmell.backend.dtos.cms.question.QuestionFilter
import no.schmell.backend.dtos.cms.question.UpdateQuestionDto
import no.schmell.backend.entities.cms.Question
import no.schmell.backend.entities.cms.QuestionFunction
import no.schmell.backend.repositories.cms.GameRepository
import no.schmell.backend.repositories.cms.QuestionFunctionRepository
import no.schmell.backend.repositories.cms.WeekRepository
import no.schmell.backend.services.files.FilesService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@Service
class QuestionsService(
    private val questionRepository: QuestionRepository,
    val weekRepository: WeekRepository,
    val gameRepository: GameRepository,
    val questionFunctionRepository: QuestionFunctionRepository,
    val filesService: FilesService,
    val gamesService: GamesService,
) {

    companion object : KLogging()

    fun getAll(filter: QuestionFilter): List<QuestionDto> {
        var questions = questionRepository.findAll()

        if (filter.relatedWeek != null) questions = questions.filter { it.relatedWeek.id == filter.relatedWeek }

        if (filter.apiFunction == "RANDOMIZE") {
            logger.info { "RANDOMIZE triggered" }
            questions = questions.shuffled()
        }

        if (filter.sort == "PHASE_ASC") {
            questions = questions.sortedBy { it.phase }
        }

        return questions.map { question -> question.toQuestionDto(filesService) }
    }

    fun createSeveral(dto: List<CreateQuestionDto>): List<QuestionDto> {
        val savedQuestions = questionRepository.saveAll(dto.map {
            val relatedWeek = weekRepository.findByIdOrNull(it.relatedWeek) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
            val relatedGame = gameRepository.findByIdOrNull(it.relatedGame) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
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
                relatedWeek,
                it.type,
                it.questionDescription,
                it.phase,
                relatedFunction,
                it.punishment,
                null,
                relatedGame
            )
        })

        gamesService.update(savedQuestions.first().relatedGame.id!!, UpdateGameDto(null, null))
        return savedQuestions.map { question -> question.toQuestionDto(filesService) }
    }

    fun getById(id: Int): QuestionDto {
        val question = questionRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return question.toQuestionDto(filesService)
    }

    fun create(dto: CreateQuestionDto): QuestionDto {
        val relatedWeek = weekRepository.findByIdOrNull(dto.relatedWeek) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val relatedGame = gameRepository.findByIdOrNull(dto.relatedGame) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        gamesService.update(relatedGame.id!!, UpdateGameDto(null, null))

        var relatedFunction: QuestionFunction? = null

        dto.function?.let { it -> relatedFunction = questionFunctionRepository.save(QuestionFunction(
            null,
            it.timer,
            it.answer,
            it.challenges,
            it.questions,
            it.options
        )) }

        return questionRepository.save(Question(
            null,
            relatedWeek,
            dto.type,
            dto.questionDescription,
            dto.phase,
            relatedFunction,
            dto.punishment,
            null,
            relatedGame
        )).toQuestionDto(filesService)
    }

    fun update(id: Int, dto: UpdateQuestionDto): QuestionDto {
        val questionToUpdate = questionRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

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
            questionToUpdate.relatedWeek,
            dto.type ?: questionToUpdate.type,
            dto.questionDescription ?: questionToUpdate.questionDescription,
            dto.phase ?: questionToUpdate.phase,
            relatedFunction ?: questionToUpdate.function,
            dto.punishment ?: questionToUpdate.punishment,
            questionToUpdate.questionPicture,
            questionToUpdate.relatedGame
        )

        gamesService.update(questionToUpdate.relatedGame.id!!, UpdateGameDto(null, null))
        return questionRepository.save(updatedQuestion).toQuestionDto(filesService)
    }

    fun addPlayerToQuestions(players: List<String>, questions: List<QuestionDto>): List<QuestionDto> {
        val updatedQuestions : MutableList<QuestionDto> = mutableListOf()

        try {
            for (question in questions) {
                val updatedQuestionDescription: String =
                    switchOutQuestionStringWithPlayers(question.questionDescription, players)
                updatedQuestions.add(
                    QuestionDto(
                        question.id,
                        question.relatedWeek,
                        question.type,
                        updatedQuestionDescription,
                        question.phase,
                        question.function,
                        question.punishment,
                        question.questionPicture,
                        question.relatedGame,
                        question.questionPicture?.let { file -> filesService.generatePresignedUrl("schmell-files", file) }
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

        val editedSecondPartOfQuestions = addPlayerToQuestions(players, secondPartOfQuestions)

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
                question.relatedWeek,
                question.type,
                question.questionDescription,
                question.phase,
                question.function,
                question.punishment,
                uploadedFile?.fileName,
                question.relatedGame,
            )).toQuestionDto(filesService)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}