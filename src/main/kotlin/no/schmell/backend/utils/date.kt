package no.schmell.backend.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjusters

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

fun getDaysInMonth(date: LocalDate = LocalDate.now()): Int = date.lengthOfMonth()
fun getWeeksInMonth(date: LocalDate = LocalDate.now()): Int = 5

val NUMBER_OF_MONTHS = 12

