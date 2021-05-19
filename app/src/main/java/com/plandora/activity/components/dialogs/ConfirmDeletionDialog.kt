package com.plandora.activity.components.dialogs

import android.app.AlertDialog
import android.content.Context

class ConfirmDeletionDialog(private val context: Context, private val listener: ConfirmDialogListener) {

    companion object {
        private const val DIALOG_TITLE: String = "Delete item"
        private const val DIALOG_MESSAGE: String = "Are you sure you want to delete this item permanently?"
        private const val POSITIVE_BUTTON_TEXT: String = "Ok"
        private const val NEGATIVE_BUTTON_TEXT: String = "Cancel"
    }

    fun showDialog() {
        AlertDialog.Builder(context)
            .setTitle(DIALOG_TITLE)
            .setMessage(DIALOG_MESSAGE)
            .setPositiveButton(POSITIVE_BUTTON_TEXT) { _, _ ->
                listener.onPositiveButtonClicked()
            }
            .setNegativeButton(NEGATIVE_BUTTON_TEXT) { _, _ ->
                listener.onNegativeButtonClicked()
            }
            .show()
    }

}