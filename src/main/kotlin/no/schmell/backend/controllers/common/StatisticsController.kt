package no.schmell.backend.controllers.common

import no.schmell.backend.dtos.common.StatisticsResponse
import no.schmell.backend.services.common.StatisticsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v2/common/statistics")
class StatisticsController(
    val statisticsService: StatisticsService
) {

    @GetMapping("")
    fun getStatistics(): StatisticsResponse = statisticsService.getStatistics()
}