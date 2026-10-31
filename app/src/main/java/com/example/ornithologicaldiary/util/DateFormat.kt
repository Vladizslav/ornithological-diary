package com.example.ornithologicaldiary.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormat {

    private val formatter = SimpleDateFormat("d MMMM yyyy", Locale.forLanguageTag("ru"))

    fun format(millis: Long): String = formatter.format(Date(millis))
}
