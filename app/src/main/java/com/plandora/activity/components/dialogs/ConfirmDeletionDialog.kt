package com.plandora.activity.components.dialogs

import android.app.AlertDialog
import android.content.Context

class ConfirmDeletionDialog(
    private val context: Context,
    private val listener: ConfirmDialogListener,
    private val dialogTitle: String,
    private val dialogMessage: String) {

    companion object {
        private const val POSITIVE_BUTTON_TEXT: String = "Ok"
        private const val NEGATIVE_BUTTON_TEXT: String = "Cancel"
    }

    fun showDialog() {
        AlertDialog.Builder(context)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setPositiveButton(POSITIVE_BUTTON_TEXT) { _, _ ->
                listener.onConfirmDeletionClicked()
            }
            .setNegativeButton(NEGATIVE_BUTTON_TEXT) { _, _ ->
                listener.onCancelDeletionClicked()
            }
            .show()
    }

}