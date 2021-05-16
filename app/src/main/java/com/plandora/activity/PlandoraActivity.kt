package com.plandora.activity

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.app_bar_main.*

open class PlandoraActivity : AppCompatActivity() {

    open fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }

}