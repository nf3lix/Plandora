package com.plandora.activity.components

interface DatePickerObserver {
    fun updateSelectedDate(selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int)
}