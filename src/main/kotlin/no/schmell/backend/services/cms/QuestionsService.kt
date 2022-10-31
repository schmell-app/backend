package no.schmell.backend.services.cms

import mu.KLogging
import no.schmell.backend.dtos.cms.CreateQuestionParams
import no.schmell.backend.repositories.cms.QuestionRepository
import no.schmell.backend.utils.switchOutQuestionStringWithPlayers
import no.schmell.backend.dtos.cms.QuestionDto
import no.schmell.backend.dtos.cms.QuestionFilter
import no.schmell.backend.dtos.cms.QuestionListDto
import no.schmell.backend.entities.cms.Question
import no.schmell.backend.lib.files.GenerateObjectSignedUrl
import no.schmell.backend.services.files.FileService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.net.URL

@Service
class QuestionsService(
    private val questionRepository: QuestionRepository,
    val weeksService: WeeksService,
    val gamesService: GamesService,
    val filesService: FileService,
    val generateObjectSignedUrl: GenerateObjectSignedUrl
) {

    companion object : KLogging()

    fun getAll(filter: QuestionFilter): List<QuestionListDto> {
        var questions = questionRepository.findAll()

        if (filter.relatedWeek != null) questions = questions.filter { it.relatedWeek.id == filter.relatedWeek }

        if (filter.apiFunction == "RANDOMIZE") {
            questions = questions.shuffled()
        }

        if (filter.sort == "PHASE_ASC") {
            questions = questions.sortedBy { it.phase }
        }

        return questions.map { question -> question.toQuestionListDto(generateObjectSignedUrl) }
    }

    fun createSeveral(dto: List<CreateQuestionParams>): List<QuestionDto> {
        val savedQuestions = questionRepository.saveAll(dto.map { question ->
            val week = weeksService.getById(question.relatedWeek)
            val game = gamesService.getById(question.relatedGame)
            question.fromCreateToDto(week, game).toQuestionEntity()
        })

        return savedQuestions.map { question -> question.toQuestionDto(generateObjectSignedUrl) }
    }

    fun getById(id: Int): QuestionDto {
        val question = questionRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return question.toQuestionDto(generateObjectSignedUrl)
    }

    fun create(dto: CreateQuestionParams): QuestionDto {
        val relatedWeek = weeksService.getById(dto.relatedWeek)
        val relatedGame = gamesService.getById(dto.relatedGame)
        return questionRepository.save(dto.fromCreateToDto(relatedWeek, relatedGame).toQuestionEntity()).toQuestionDto(generateObjectSignedUrl)
    }

    fun update(id: Int, question: QuestionDto): QuestionDto {
        return if (questionRepository.existsById(id)) {
            questionRepository.save(question.toQuestionEntity()).toQuestionDto(generateObjectSignedUrl)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun addPlayerToQuestions(players: List<String>, questions: List<QuestionListDto>): List<QuestionListDto> {
        val updatedQuestions : MutableList<QuestionListDto> = mutableListOf()

        try {
            for (question in questions) {
                val updatedQuestionDescription: String =
                    switchOutQuestionStringWithPlayers(question.questionDescription, players)
                updatedQuestions.add(
                    QuestionListDto(
                        question.id,
                        question.relatedWeek,
                        question.type,
                        updatedQuestionDescription,
                        question.phase,
                        question.function,
                        question.punishment,
                        question.questionPicture,
                        generateObjectSignedUrl.generateSignedUrl(question.questionPicture),
                        question.relatedGame
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
        uneditedQuestions: List<QuestionListDto>,
        editedQuestions: List<QuestionListDto>
    ): List<QuestionListDto> {
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
            val uploadedFile = filesService.uploadFile(file, "question_pictures")

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
            )).toQuestionDto(generateObjectSignedUrl)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}