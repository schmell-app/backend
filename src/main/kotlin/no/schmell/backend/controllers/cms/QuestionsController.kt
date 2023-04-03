package no.schmell.backend.controllers.cms

import no.schmell.backend.dtos.cms.*
import no.schmell.backend.dtos.cms.question.*
import no.schmell.backend.services.cms.QuestionsService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("cms/question")
@CrossOrigin(origins = ["http://localhost:3000", "https://admin.dev.schmell.no", "https://admin.schmell.no"])
class QuestionsController(val questionsService: QuestionsService) {

    @GetMapping("/{id}/")
    fun getQuestion(@PathVariable("id") id: String): QuestionDto =
        questionsService.getById(id.toInt())

    @GetMapping("/")
    fun getQuestions(
        @RequestParam(value = "relatedWeek", required = false) relatedWeek: String?,
        @RequestParam(value = "sort", required = false) sort: String?,
        @RequestParam(value = "apiFunction", required = false) apiFunction: String?,
        ): List<QuestionDto> =
        questionsService.getAll(QuestionFilter(relatedWeek?.toInt(), sort, apiFunction))

    @PostMapping("/play/")
    fun getQuestionsForPlay(
        @RequestBody(required = true) dto: PlayQuestionParams
    ): PlayQuestionsResponse =
        questionsService.getQuestionsForPlay(dto)

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createQuestion(@RequestBody(required = true) dto: CreateQuestionDto): QuestionDto =
        questionsService.create(dto)

    @PostMapping("/many/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createMany(@RequestBody(required = true) dto: List<CreateQuestionDto>): List<QuestionDto> =
        questionsService.createSeveral(dto)

    @PatchMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    fun updateQuestion(@PathVariable("id") id: String, @RequestBody(required = true) dto: UpdateQuestionDto): QuestionDto =
        questionsService.update(id.toInt(), dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteQuestion(@PathVariable("id", required = true ) id: String) = questionsService.delete(id.toInt())

    @PostMapping("/add/player/inGame/")
    @ResponseStatus(HttpStatus.OK)
    fun addPlayerInGame(@RequestBody(required = true) dto: AddPlayersInGameParams): List<QuestionDto> =
        questionsService.addPlayerToQuestionsInGame(dto.players, dto.currentIndex, dto.uneditedQuestions, dto.editedQuestions)

    @PostMapping("/{id}/files/")
    @ResponseStatus(HttpStatus.OK)
    fun addQuestionPicture(@PathVariable("id") id: String, @RequestBody file: MultipartFile): QuestionDto =
        questionsService.addQuestionPicture(id.toInt(), file)

}