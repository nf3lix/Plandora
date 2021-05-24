package com.plandora.activity.main.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.plandora.R
import com.plandora.activity.PlandoraActivity
import com.plandora.adapters.EventRecyclerAdapter
import com.plandora.controllers.EventController
import com.plandora.models.events.Event
import com.plandora.models.events.EventType
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.activity_search_for_events.*
import kotlinx.android.synthetic.main.app_bar_main.*

class SearchForEventsActivity: PlandoraActivity(), EventRecyclerAdapter.OnClickListener {

    private lateinit var rootView: View
    private lateinit var eventAdapter: EventRecyclerAdapter
    private lateinit var eventList: ArrayList<Event>

    private var eventTypeList: ArrayList<String> = ArrayList()
    private lateinit var filteredEventList: ArrayList<Event>

    private var eventTypeSelected: Boolean = false
    private var eventTitleSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_events)
        //addActionBar()
        EventController().updateEventList()
        eventList = EventController.eventList
        getEventTypes()
        search_type_spinner.adapter =
            ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, eventTypeList)
        setupButtonListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_for_events_menu, menu)
        return true
    }

    private fun getEventTypes() {
        eventTypeList.add("")
        for (event in eventList) {
            if (!eventTypeList.contains(event.eventType.name)) {
                eventTypeList.add(event.eventType.name)
            }
        }
    }

    override fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }

    private fun setupButtonListeners() {
        btn_search_for_events.setOnClickListener {
            eventTitleSelected = !search_for_events_title_input.text.toString().equals("")
            eventTypeSelected = !search_type_spinner.selectedItem.toString().equals("")
            addEventRecyclerView()
        }
    }

    override fun onClickListener(index: Int) {
        startEventDetailActivity(filteredEventList[index])
    }

    private fun startEventDetailActivity(event: Event) {
        val intent = Intent(this, EventDetailActivity::class.java)
        intent.putExtra("event_object", event)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.close_search -> {
                finish()
                true
            }
            else -> false
        }
    }

    private fun addEventRecyclerView() {
        eventList = EventController.eventList
        if(eventTitleSelected || eventTypeSelected) {
            getFilteredEventList()
        } else {
            filteredEventList = eventList
        }
        if(filteredEventList.isEmpty()){
            Toast.makeText(this, "No matching Events found.", Toast.LENGTH_SHORT).show()
        }
        this.findViewById<RecyclerView>(R.id.search_for_events_recycler_view).apply {
            layoutManager = LinearLayoutManager(this@SearchForEventsActivity)
            eventAdapter = EventRecyclerAdapter(filteredEventList, this@SearchForEventsActivity)
            adapter = eventAdapter
        }
    }

    private fun getFilteredEventList() {
        filteredEventList = ArrayList()
        for (event in eventList) {
            if (matchingEvent(event)) {
                filteredEventList.add(event)
            }
        }
    }

    private fun matchingEvent(event: Event): Boolean {
        return if (eventTypeSelected) {
            if (event.eventType.toString() == search_type_spinner.selectedItem.toString()) {
                if (eventTitleSelected) {
                    event.title == search_for_events_title_input.text.toString()
                } else {
                    true
                }
            } else {
                false
            }
        } else {
            event.title == search_for_events_title_input.text.toString()
        }
    }

}
