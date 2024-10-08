package no.schmell.backend.controllers.cms

import no.schmell.backend.dtos.cms.*
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
        @RequestParam(value = "relatedGame", required = false) relatedGame: String?,
        @RequestParam(value = "questionType", required = false) questionType: String?,
        @RequestParam(value = "questionSearch", required = false) questionSearch: String?,
        @RequestParam(value = "hasDislikes", required = false) hasDislikes: String?,
        @RequestParam(value = "dislikesGreaterThan", required = false) dislikesGreaterThan: String?,
        @RequestParam(value = "page", required = false, defaultValue = "1") page: String = "1",
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") pageSize: String = "10"
        ): QuestionPaginatedResponse =
        questionsService.getAll(QuestionFilter(
            relatedGame?.toInt(),
            questionType?.toInt(),
            questionSearch,
            hasDislikes?.toBoolean(),
            dislikesGreaterThan?.toInt(),
            page.toInt(),
            pageSize.toInt()
        ))

    @PostMapping("/play/")
    fun startGame(
        @RequestBody(required = true) dto: GamePlayParams
    ): GamePlayResponse =
        questionsService.startGame(dto)

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

    @PutMapping("/{id}/dislike/add/")
    @ResponseStatus(HttpStatus.OK)
    fun addDislike(@PathVariable("id") id: String): QuestionDto =
        questionsService.addDislike(id.toInt())

    @PutMapping("/{id}/dislike/remove/")
    @ResponseStatus(HttpStatus.OK)
    fun removeDislike(@PathVariable("id") id: String): QuestionDto =
        questionsService.removeDislike(id.toInt())
}