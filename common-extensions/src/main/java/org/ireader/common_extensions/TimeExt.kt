package org.ireader.common_extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.US)
    return format.format(date)
}

fun convertLongToTime(time: Long, format: String = "yyyy.MM.dd HH:mm"): String {
    val date = Date(time)
    val format = SimpleDateFormat(format, Locale.getDefault())
    return format.format(date)
}

fun currentTimeToLong(): Long {
    return Calendar.getInstance().timeInMillis
}

fun convertDateToLong(date: String): Long? {
    val df = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.US)
    return df.parse(date)?.time
}
