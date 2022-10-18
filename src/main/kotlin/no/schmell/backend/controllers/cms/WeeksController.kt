package no.schmell.backend.controllers.cms

import no.schmell.backend.dtos.cms.CreateWeekParams
import no.schmell.backend.dtos.cms.WeekDto
import no.schmell.backend.dtos.cms.WeekFilters
import no.schmell.backend.services.cms.WeeksService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v2/cms/week")
class WeeksController(val weeksService: WeeksService) {

    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    fun getWeek(@PathVariable("id") id: String): WeekDto = weeksService.getById(id.toInt())

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun getWeeks(
        @RequestParam(value = "relatedGame", required = false) relatedGame: String?,
        @RequestParam(value = "weekNumber", required = false) weekNumber: String?
    ): List<WeekDto> =
        weeksService.getAll(WeekFilters(relatedGame?.toInt(), weekNumber?.toInt()))

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createWeek(@RequestBody(required = true) dto: CreateWeekParams): WeekDto =
        weeksService.create(dto)

    @PutMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    fun updateWeek(@PathVariable("id") id: String, @RequestBody dto: WeekDto): WeekDto =
        weeksService.update(id.toInt(), dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteGame(@PathVariable("id") id: String) = weeksService.delete(id.toInt())
}