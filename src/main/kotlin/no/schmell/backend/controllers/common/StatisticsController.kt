package no.schmell.backend.controllers.common

import no.schmell.backend.dtos.common.StatisticsFilter
import no.schmell.backend.dtos.common.StatisticsGeneralResponse
import no.schmell.backend.dtos.common.StatisticsOverviewResponse
import no.schmell.backend.lib.enums.StatisticsUserView
import no.schmell.backend.lib.enums.StatisticsView
import no.schmell.backend.services.common.StatisticsService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("common/statistics/")
@CrossOrigin(origins = ["http://localhost:3000", "https://admin.dev.schmell.no", "https://admin.schmell.no"])
class StatisticsController(
    val statisticsService: StatisticsService
) {

    @GetMapping("general/")
    fun getGeneralStatistics(): StatisticsGeneralResponse = statisticsService.getGeneralStatistics()

    @GetMapping("overview/")
    fun getOverviewStatistics(
        @RequestParam(value = "users", required = false) users: String?,
        @RequestParam(value = "popularity", required = false) popularity: String?,
        @RequestParam(value = "income", required = false) income: String?,
    ): StatisticsOverviewResponse = statisticsService.getOverviewStatistics(
        StatisticsFilter(
            StatisticsUserView.valueOf(users ?: "MonthWeek"),
            StatisticsView.valueOf(popularity ?: "Month"),
            StatisticsView.valueOf(income ?: "Month")
        )
    )
}