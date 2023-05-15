package no.schmell.backend.controllers.cms

import no.schmell.backend.dtos.cms.QuestionTypeDto
import no.schmell.backend.dtos.cms.QuestionTypeFilter
import no.schmell.backend.dtos.cms.UpdateQuestionTypeDto
import no.schmell.backend.services.cms.QuestionTypeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("cms/question/type")
@CrossOrigin(origins = ["http://localhost:3000", "https://admin.dev.schmell.no", "https://admin.schmell.no"])
class QuestionTypeController(val questionTypeService: QuestionTypeService) {

    @GetMapping("/")
    fun getQuestionTypes(
        @RequestParam(value = "name", required = false) nameSearch: String?
    ): List<QuestionTypeDto> = questionTypeService.get(QuestionTypeFilter(nameSearch))

    @PostMapping("/")
    fun create(@RequestBody(required = true) dto: QuestionTypeDto): QuestionTypeDto =
        questionTypeService.create(dto)

    @PatchMapping("/{id}/")
    fun update(@PathVariable("id") id: String, @RequestBody(required = true) dto: UpdateQuestionTypeDto): QuestionTypeDto =
        questionTypeService.update(id.toInt(), dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("id") id: String) = questionTypeService.delete(id.toInt())
}