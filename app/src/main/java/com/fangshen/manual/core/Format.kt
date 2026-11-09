package com.fangshen.manual.core

import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val MILLIS_PER_HOUR = 3_600_000L

/** 把时间戳格式化为条目用的展示文本，如 “2026年11月6日 · 昨天 11:44” */
fun formatEventTime(millis: Long, now: Long = System.currentTimeMillis()): String {
    val date = Date(millis)
    val dayPart = when {
        isSameDay(millis, now) -> ""
        isSameDay(millis, now - MILLIS_PER_HOUR * 24) -> "昨天 "
        isSameDay(millis, now + MILLIS_PER_HOUR * 24) -> "明天 "
        else -> ""
    }
    val dateText = SimpleDateFormat("yyyy年M月d日", Locale.CHINA).format(date)
    val timeText = SimpleDateFormat("HH:mm", Locale.CHINA).format(date)
    return if (dayPart.isEmpty()) "$dateText · $timeText" else "$dateText · $dayPart$timeText"
}

/** 仅日期，如 “11月6日” */
fun formatDayLabel(millis: Long, now: Long = System.currentTimeMillis()): String {
    val dayDiff = dayDifference(millis, now)
    return when {
        dayDiff <= 0 -> "今天"
        dayDiff == 1 -> "昨天"
        dayDiff == 2 -> "前天"
        else -> dayDiff.toString() + "日前"
    }
}

/** 按自然日计算相差天数（a 早于 b 为正） */
private fun dayDifference(a: Long, b: Long): Int {
    val calA = Calendar.getInstance().apply { timeInMillis = a }
    val calB = Calendar.getInstance().apply { timeInMillis = b }
    val dayA = calA.get(Calendar.YEAR) * 366 + calA.get(Calendar.DAY_OF_YEAR)
    val dayB = calB.get(Calendar.YEAR) * 366 + calB.get(Calendar.DAY_OF_YEAR)
    return dayB - dayA
}

/** 剂量数字格式化（BigDecimal，避免浮点误差）：12.50 -> 12.5 */
fun formatDoseBD(amount: BigDecimal): String =
    if (amount.compareTo(BigDecimal.ZERO) == 0) "0"
    else amount.stripTrailingZeros().toPlainString()

/** 完整日期时间（条目详情页） */
fun formatFullDateTime(millis: Long): String =
    SimpleDateFormat("yyyy年M月d日 HH:mm", Locale.CHINA).format(Date(millis))

/** 时长（如语音时长） */
fun formatDuration(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return if (minutes > 0) "${minutes}分${seconds}秒" else "${seconds}秒"
}

/** 剂量数字格式化：避免无意义的小数尾零 */
fun formatDose(amount: Double): String =
    if (amount % 1.0 == 0.0) "${amount.toLong()}" else amount.toString()

private fun isSameDay(a: Long, b: Long): Boolean {
    val calA = Calendar.getInstance().apply { timeInMillis = a }
    val calB = Calendar.getInstance().apply { timeInMillis = b }
    return calA.get(Calendar.YEAR) == calB.get(Calendar.YEAR) &&
        calA.get(Calendar.DAY_OF_YEAR) == calB.get(Calendar.DAY_OF_YEAR)
}
