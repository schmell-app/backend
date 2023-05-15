package no.schmell.backend.utils

import no.schmell.backend.lib.enums.StatisticsView
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*

fun getFirstAndLastDayOfMonth(date: LocalDate = LocalDate.now()): Pair<LocalDateTime, LocalDateTime> {
    val firstDayOfMonth = date.withDayOfMonth(1)
    val lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth())

    return Pair(
        firstDayOfMonth.atStartOfDay(),
        lastDayOfMonth.atTime(LocalTime.MAX)
    )
}

fun getFirstAndLastDayOfYear(date: LocalDate = LocalDate.now()): Pair<LocalDateTime, LocalDateTime> {
    val firstDayOfYear = date.withDayOfYear(1)
    val lastDayOfYear = date.withDayOfYear(date.lengthOfYear())

    return Pair(
        firstDayOfYear.atStartOfDay(),
        lastDayOfYear.atTime(LocalTime.MAX)
    )
}

fun getFirstAndLastDayOfWeek(date: LocalDate = LocalDate.now()): Pair<LocalDateTime, LocalDateTime> {
    val firsDayOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val lastDayOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    return Pair(
        firsDayOfWeek.atStartOfDay(),
        lastDayOfWeek.atTime(LocalTime.MAX)
    )
}

fun getDaysInMonth(date: LocalDate = LocalDate.now()): Int = YearMonth.from(date).lengthOfMonth()
fun getWeeksInMonth(date: LocalDate = LocalDate.now()): Int {
    val firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth())
    val lastDayOfMonth = date.with(TemporalAdjusters.lastDayOfMonth())
    val weekFields = WeekFields.of(Locale.getDefault())
    val firstWeek = firstDayOfMonth.get(weekFields.weekOfMonth())
    val lastWeek = lastDayOfMonth.get(weekFields.weekOfMonth())

    return lastWeek - firstWeek + 1
}

const val NUMBER_OF_MONTHS = 12
val TIME_INTERVALS = listOf("00-02", "02-04", "04-06", "06-08", "08-10", "10-12", "12-14", "14-16", "16-18", "18-20", "20-22", "22-24")

fun getRange(wantedType: StatisticsView, date: LocalDate = LocalDate.now()): Pair<LocalDateTime, LocalDateTime> {
    return when (wantedType) {
        StatisticsView.Month -> getFirstAndLastDayOfMonth(date)
        StatisticsView.Week -> getFirstAndLastDayOfWeek(date)
        StatisticsView.Year -> getFirstAndLastDayOfYear(date)
    }
}
