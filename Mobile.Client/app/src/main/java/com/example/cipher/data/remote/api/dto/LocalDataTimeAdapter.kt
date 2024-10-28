package com.example.cipher.data.remote.api.dto

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LocalDateTimeAdapter {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @FromJson
    fun fromJson(value: String): LocalDateTime = LocalDateTime.parse(value, formatter)

    @ToJson
    fun toJson(value: LocalDateTime): String = value.format(formatter)
}