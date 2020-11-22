package com.plandora.activity.main.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.plandora.R
import com.plandora.activity.CreateEventActivity
import com.plandora.models.DataSource
import com.plandora.models.Event
import com.plandora.adapters.EventRecyclerAdapter

class DashboardFragment : Fragment(), EventRecyclerAdapter.OnClickListener {

    private lateinit var rootView: View
    private lateinit var eventAdapter: EventRecyclerAdapter
    private lateinit var items: ArrayList<Event>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_dashboard_main, container, false)
        items = DataSource.createDataSet()
        addRecyclerView()
        rootView.findViewById<FloatingActionButton>(R.id.fab_create_board)
            .setOnClickListener { startActivity(Intent(rootView.context, CreateEventActivity::class.java )) }
        return rootView
    }

    private fun addRecyclerView() {
        rootView.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(EventItemSpacingDecoration(40))
            eventAdapter = EventRecyclerAdapter(items, this@DashboardFragment)
            adapter = eventAdapter
        }
    }

    override fun onClickListener(index: Int) {
        val intent = Intent(rootView.context, EventDetailActivity::class.java)
        intent.putExtra("event_object", items[index])
        startActivity(intent)
    }

}