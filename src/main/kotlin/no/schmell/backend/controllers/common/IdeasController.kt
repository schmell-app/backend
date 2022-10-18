package no.schmell.backend.controllers.common

import no.schmell.backend.dtos.common.CreateIdeaParams
import no.schmell.backend.dtos.common.IdeaDto
import no.schmell.backend.dtos.common.IdeaFilters
import no.schmell.backend.entities.common.Idea
import no.schmell.backend.lib.enums.IdeaCategory
import no.schmell.backend.services.common.IdeasService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v2/common/idea")
class IdeasController(val ideasService: IdeasService) {

    @GetMapping("/{id}/")
    fun getIdea(@PathVariable("id") id: String): IdeaDto = ideasService.getById(id.toInt())

    @GetMapping("")
    fun getIdeas(
        @RequestParam(value = "category", required = false) category: IdeaCategory? = null,
        @RequestParam(value = "createdBy", required = false) createdBy: Int? = null): List<IdeaDto> =
        ideasService.getAll(IdeaFilters(category, createdBy))

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createIdea(@RequestBody dto: CreateIdeaParams): IdeaDto = ideasService.create(dto)

    @PutMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    fun updateIdea(@PathVariable("id") id: String, @RequestBody dto: IdeaDto): IdeaDto =
        ideasService.update(id.toInt(), dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteIdea(@PathVariable("id") id: String) = ideasService.delete(id.toInt())
}