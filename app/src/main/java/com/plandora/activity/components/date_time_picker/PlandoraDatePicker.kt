package com.plandora.activity.components.date_time_picker

import android.app.DatePickerDialog
import android.content.Context
import com.plandora.R

class PlandoraDatePicker(
    private val context: Context,
    private val observer: DatePickerObserver
) {

    fun showDialog(initYear: Int, initMonth: Int, initDayOfMonth: Int) {
        val datePickerDialog = DatePickerDialog(context, R.style.SpinnerDatePickerStyle,
            { _, year, month, dayOfMonth ->
                observer.updateSelectedDate(year, month, dayOfMonth)
            }, initYear, initMonth, initDayOfMonth)
        datePickerDialog.show()
    }

}