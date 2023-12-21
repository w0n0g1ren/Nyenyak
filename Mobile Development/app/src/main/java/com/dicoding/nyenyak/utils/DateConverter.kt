package com.dicoding.nyenyak.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateConverter {
    fun convertMillisToString(timeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(calendar.time)
    }
}