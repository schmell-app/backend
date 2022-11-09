package no.schmell.backend.controllers.cms

import no.schmell.backend.dtos.cms.week.CreateWeekDto
import no.schmell.backend.dtos.cms.week.WeekDto
import no.schmell.backend.dtos.cms.week.WeekFilters
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
    fun createWeek(@RequestBody(required = true) dto: CreateWeekDto): WeekDto =
        weeksService.create(dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWeek(@PathVariable("id") id: String) = weeksService.delete(id.toInt())
}