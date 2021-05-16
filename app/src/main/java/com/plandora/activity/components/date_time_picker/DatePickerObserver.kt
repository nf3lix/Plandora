package com.plandora.activity.components.date_time_picker

interface DatePickerObserver {
    fun updateSelectedDate(selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int)
}