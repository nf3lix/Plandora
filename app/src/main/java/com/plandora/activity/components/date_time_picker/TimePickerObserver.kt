package com.plandora.activity.components.date_time_picker

interface TimePickerObserver {
    fun updateSelectedTime(selectedHour: Int, selectedMinute: Int)
}