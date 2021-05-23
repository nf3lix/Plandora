package com.plandora.activity.main.dashboard

import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import com.plandora.R
import com.plandora.activity.PlandoraActivity
import kotlinx.android.synthetic.main.activity_search_for_events.*
import kotlinx.android.synthetic.main.app_bar_main.*

class SearchForEventsActivity: PlandoraActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_events)
        //event_type_spinner.adapter = ArrayAdapter<EventType>(this, R.layout.support_simple_spinner_dropdown_item, EventType.values())
        search_type_spinner.adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayListOf("Hallo", "Test"))
        addActionBar()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_tool_bar, menu)
        return true
    }

    override fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }


}