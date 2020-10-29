package com.plandora.models

import java.util.*
import kotlin.math.roundToInt

class Event(
    val title: String,
    val eventType: EventType,
    val description: String,
    val annual: Boolean,
    timestamp: Long
) {

    companion object {
        const val MILLIS_PER_DAY = 864e5
    }

    var remainingDays: Int

    init {
        remainingDays = calculateRemainingDays(timestamp)
    }

    private fun calculateRemainingDays(timestamp: Long): Int {
        val diff = timestamp - System.currentTimeMillis()
        return when(annual && diff < 0) {
            true -> millisToDays(getNextEvent(timestamp))
            else -> millisToDays(diff)
        }
    }

    private fun getNextEvent(timestamp: Long): Long {
        val date = Date(timestamp)
        val calendar = GregorianCalendar()
        calendar.apply {
            time = date
            set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) + 1)
        }
        return calendar.timeInMillis - System.currentTimeMillis()
    }

    private fun millisToDays(millis: Long): Int {
        return (millis / MILLIS_PER_DAY).roundToInt()
    }
    
}