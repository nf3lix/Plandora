package com.plandora.activity.components

import android.app.TimePickerDialog
import android.content.Context

class PlandoraTimePicker(
    private val context: Context,
    private val observer: TimePickerObserver
) {

    companion object {
        private const val TIME_FORMAT_24HOUR = true
    }

    fun showDialog() {
        val datePickerDialog = TimePickerDialog(context,
            { _, hour, minute ->
                observer.updateSelectedTime(hour, minute)
            }, 0, 0, TIME_FORMAT_24HOUR)
        datePickerDialog.show()
    }

}