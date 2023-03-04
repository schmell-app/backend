package no.schmell.backend.controllers.common

import no.schmell.backend.dtos.common.StatisticsResponse
import no.schmell.backend.services.common.StatisticsService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v2/common/statistics/")
@CrossOrigin(origins = ["http://localhost:3000", "https://admin.dev.schmell.no", "https://admin.schmell.no"])
class StatisticsController(
    val statisticsService: StatisticsService
) {

    @GetMapping("")
    fun getStatistics(): StatisticsResponse = statisticsService.getStatistics()
}