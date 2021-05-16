package com.plandora.activity.components

interface TimePickerObserver {
    fun updateSelectedTime(selectedHour: Int, selectedMinute: Int)
}