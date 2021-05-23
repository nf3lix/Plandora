package com.plandora.activity.main.dashboard

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.plandora.R
import com.plandora.activity.PlandoraActivity
import com.plandora.models.events.EventType
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.app_bar_main.*

class SearchForEventsActivity: PlandoraActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_events)
        //event_type_spinner.adapter = ArrayAdapter<EventType>(this, R.layout.support_simple_spinner_dropdown_item, EventType.values())
        event_type_spinner.adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayListOf("Hallo", "Test"))
        addActionBar()

    }

    override fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }


}