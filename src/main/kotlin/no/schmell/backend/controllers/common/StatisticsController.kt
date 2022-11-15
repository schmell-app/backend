package no.schmell.backend.controllers.common

import com.amazonaws.Response
import no.schmell.backend.dtos.common.StatisticsResponse
import no.schmell.backend.services.common.StatisticsService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v2/common/statistics/")
@CrossOrigin("http://localhost:3000")
class StatisticsController(
    val statisticsService: StatisticsService
) {

    @GetMapping("")
    fun getStatistics(): StatisticsResponse = statisticsService.getStatistics()
}