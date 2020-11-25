package com.plandora.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.plandora.R
import kotlinx.android.synthetic.main.app_bar_main.*

open class PlandoraActivity : AppCompatActivity() {

    open fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }

}