package com.plandora.activity.main.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.plandora.R
import com.plandora.activity.CreateEventActivity
import com.plandora.models.events.Event
import com.plandora.adapters.EventRecyclerAdapter
import com.plandora.controllers.PlandoraEventController

class DashboardFragment : Fragment(), EventRecyclerAdapter.OnClickListener {

    private lateinit var rootView: View
    private lateinit var eventAdapter: EventRecyclerAdapter
    private lateinit var eventList: ArrayList<Event>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        displayDashboardFragment(inflater, container)
        addFabButtonListener()
        return rootView
    }

    override fun onStart() {
        super.onStart()
        addEventRecyclerView()
    }

    override fun onClickListener(index: Int) {
        startEventDetailActivity(eventList[index])
    }

    private fun addEventRecyclerView() {
        eventList = PlandoraEventController.eventList
        rootView.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(activity)
            eventAdapter = EventRecyclerAdapter(eventList, this@DashboardFragment)
            adapter = eventAdapter
        }
    }

    private fun startEventDetailActivity(event: Event) {
        val intent = Intent(rootView.context, EventDetailActivity::class.java)
        intent.putExtra("event_object", event)
        startActivity(intent)
    }

    private fun displayDashboardFragment(inflater: LayoutInflater, container: ViewGroup?) {
        rootView = inflater.inflate(R.layout.fragment_dashboard_main, container, false)
        rootView.findViewById<RecyclerView>(R.id.recycler_view).addItemDecoration(EventItemSpacingDecoration(40))
    }

    private fun addFabButtonListener() {
        rootView.findViewById<FloatingActionButton>(R.id.fab_create_board)
                .setOnClickListener { startActivity(Intent(rootView.context, CreateEventActivity::class.java )) }
    }

}