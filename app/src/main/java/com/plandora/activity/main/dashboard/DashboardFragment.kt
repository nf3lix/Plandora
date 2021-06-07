package com.plandora.activity.main.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.plandora.R
import com.plandora.activity.CreateEventActivity
import com.plandora.adapters.EventRecyclerAdapter
import com.plandora.controllers.EventController
import com.plandora.controllers.State
import com.plandora.models.events.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(), EventRecyclerAdapter.OnClickListener {

    private lateinit var rootView: View
    private lateinit var eventAdapter: EventRecyclerAdapter
    private lateinit var eventList: ArrayList<Event>

    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        displayDashboardFragment(inflater, container)
        setHasOptionsMenu(true)
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

    override fun onResume() {
        super.onResume()
        uiScope.launch {
            loadEvents()
        }
    }

    private fun addEventRecyclerView() {
        eventList = EventController.eventList
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

    private fun startSearchForEventsActivity() {
        val intent = Intent(rootView.context, SearchForEventsActivity::class.java)
        startActivity(intent)
    }

    private fun displayDashboardFragment(inflater: LayoutInflater, container: ViewGroup?) {
        rootView = inflater.inflate(R.layout.fragment_dashboard_main, container, false)
        rootView.findViewById<RecyclerView>(R.id.recycler_view).addItemDecoration(EventItemSpacingDecoration(40))
    }

    private fun addFabButtonListener() {
        rootView.findViewById<FloatingActionButton>(R.id.fab_create_board)
            .setOnClickListener {
                startActivity(Intent(rootView.context, CreateEventActivity::class.java))
            }
    }

    private suspend fun loadEvents() {
        EventController().updateEventList().collect { state ->
            when(state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    addEventRecyclerView()
                }
                is State.Failed -> {
                    //
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_tool_bar, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.search_events -> {
                startSearchForEventsActivity()
                true
            }
            else -> false
        }
    }

}