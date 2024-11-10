package com.example.cipher.ui.common.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LastSeenFormatter {

    fun getLastSeenMessage(lastSeen: LocalDateTime): String {
        val currentDate = LocalDateTime.now().toLocalDate()
        val targetDate = lastSeen.toLocalDate()

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")

        val message = when {
            targetDate.isEqual(currentDate) -> {
                "today at ${lastSeen.format(timeFormatter)}"
            }
            targetDate.isEqual(currentDate.minusDays(1L)) -> {
                "yesterday at ${lastSeen.format(timeFormatter)}"
            }
            targetDate.isAfter(currentDate.minusMonths(3L)) -> {
                "on ${lastSeen.format(dateFormatter)}"
            }
            else -> {
                "a long time ago"
            }
        }

        return "Last seen $message"
    }

}