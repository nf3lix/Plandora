package com.plandora.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import com.plandora.R
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.dialog_progress.view.*

open class PlandoraActivity : AppCompatActivity() {

    private lateinit var progressBar: Dialog

    open fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }

    fun showProgressBar(text: String = "Loading...") {
        progressBar = Dialog(this)
        progressBar.setContentView(R.layout.dialog_progress)
        progressBar.progress_text.text = text
        progressBar.show()
    }

    fun hideProgressBar() {
        progressBar.dismiss()
    }

}