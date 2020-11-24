package com.plandora.activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class PlandoraDialog(context: Context, val view: ViewGroup?, private val attachToRoot: Boolean, val resource: Int, val title: String) :
    AlertDialog.Builder(context) {

    val viewInflated: View = LayoutInflater.from(context).inflate(resource, view, attachToRoot)

    open fun showDialog() {
        setTitle(title)
        setView(viewInflated)
        setPositiveButton("Ok") { dialog, which -> onPositiveButtonClick(dialog, which) }
        setNegativeButton("Cancel") { dialog, which -> onNegativeButtonClick(dialog, which) }
        show()
    }

    abstract fun onPositiveButtonClick(dialog: DialogInterface, which: Int)
    abstract fun onNegativeButtonClick(dialog: DialogInterface, which: Int)

}