package com.jun.template.common.extension

import com.blankj.utilcode.constant.TimeConstants
import java.util.*

const val SEC = 1000
const val MIN = 60 * 1000
const val HOUR = 60 * 60 * 1000
const val DAY = 24 * 60 * 60 * 1000
fun Long.toTimeSpan(): String {

    val now = System.currentTimeMillis()
    val calendar = Calendar.getInstance()
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.MILLISECOND] = 0
    val week: Long = calendar.timeInMillis
    val span: Long = now - this
    when {
        span < 0 -> {
            return String.format("%tF %tR", this, this)
        }
        span < SEC -> {
            return "Just now"
        }
        span < MIN -> {
            return String.format(Locale.getDefault(), "%d seconds ago", span / SEC)
        }
        span < HOUR -> {
            return String.format(
                Locale.ENGLISH,
                "%d minute%s ago",
                span / MIN,
                if (span / MIN > 1) "s" else ""
            )
        }
        span < DAY / 2 -> {
            return String.format(
                Locale.ENGLISH,
                "%d hour%s ago",
                span / HOUR,
                if (span / HOUR > 1) "s" else ""
            )
        }
        this >= week -> {
            return String.format("Today %tR", this)
        }
        this >= week - TimeConstants.DAY -> {
            return String.format("Yesterday %tR", this)
        }
        else -> {
            return String.format("%tF %tR", this, this)
        }
    }
}