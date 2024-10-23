package com.example.cipher.ui.common.utils

import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object LastSeenFormatter {

    fun getLastSeenMessage(timestamp: Timestamp): String {
        val currentDate = LocalDateTime.now().toLocalDate()
        val targetDateTime = Instant.ofEpochMilli(timestamp.time)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        val targetDate = targetDateTime.toLocalDate()


        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")

        val message = when {
            targetDate.isEqual(currentDate) -> {
                "today at ${targetDateTime.format(timeFormatter)}"
            }
            targetDate.isEqual(currentDate.minusDays(1L)) -> {
                "yesterday at ${targetDateTime.format(timeFormatter)}"
            }
            targetDate.isAfter(currentDate.minusMonths(3L)) -> {
                "on ${targetDateTime.format(dateFormatter)}"
            }
            else -> {
                "a long time ago"
            }
        }

        return "Last seen $message"
    }

}