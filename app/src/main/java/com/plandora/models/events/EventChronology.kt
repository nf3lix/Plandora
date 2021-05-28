package com.plandora.models.events

import java.text.SimpleDateFormat
import java.util.*

class EventChronology private constructor() {

    companion object {

        fun eventChronologyFromEvent(event: Event): EventChronology {
            val chronology = EventChronology()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = event.timestamp
            chronology.apply {
                year = calendar.get(Calendar.YEAR)
                monthOfYear = calendar.get(Calendar.MONTH) + 1
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                minute = calendar.get(Calendar.MINUTE)
                hour = calendar.get(Calendar.HOUR_OF_DAY)
            }
            return chronology
        }

        fun eventChronologyFromCurrentTimestamp(): EventChronology {
            val chronology = EventChronology()
            chronology.apply {
                year = Calendar.getInstance().get(Calendar.YEAR)
                monthOfYear = Calendar.getInstance().get(Calendar.MONTH) + 1
                dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                hour = 0
                minute = 0
            }
            return chronology
        }
    }

    var year = 0
    var monthOfYear = 0
    var dayOfMonth = 0
    var hour = 0
    var minute = 0

    fun getTimestamp(): Long {
        return SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US)
            .parse("${dayOfMonth.toString().format(2)}-" +
                    "${monthOfYear.toString().format(2)}-" +
                    "${year.toString().format(4)} " +
                    "${hour.toString().format(2)}:" +
                    minute.toString().format(2)
            )!!.time
    }

}