package com.example.cipher.ui.common.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MessagesDateFormatter {

    fun getMessageDate(date: LocalDateTime): String {
        val currentDate = LocalDateTime.now().toLocalDate()
        val targetDate = date.toLocalDate()

        val dateFormatter = DateTimeFormatter.ofPattern("MMMM d")
        val dateFormatterWithYear = DateTimeFormatter.ofPattern("MMMM d, yyyy")

        val message = when {
            targetDate.isEqual(currentDate) -> {
                "Today"
            }
            targetDate.isEqual(currentDate.minusDays(1L)) -> {
                "Yesterday"
            }
            targetDate.isAfter(currentDate.minusYears(1L)) -> {
                targetDate.format(dateFormatter)
            }
            else -> {
                targetDate.format(dateFormatterWithYear)
            }
        }

        return message
    }

}