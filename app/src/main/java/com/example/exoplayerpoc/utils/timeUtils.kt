package com.example.exoplayerpoc.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getTimeMs(atMidnight: Boolean, daysOffset: Int): Long {
    if (!atMidnight) {
        return System.currentTimeMillis()
    }
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = Date(System.currentTimeMillis())
    val c = Calendar.getInstance()
    c.time = date
    c.add(Calendar.DATE, daysOffset)
    val offset = c.time
    val formatted = formatter.format(offset)
    val reparsed = formatter.parse(formatted)
    return reparsed?.time ?: 0
}