package com.plandora.activity.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.plandora.R
import com.plandora.models.DataSource
import com.plandora.models.EventRecyclerAdapter
import kotlinx.android.synthetic.main.activity_recycler_view_test.*
import kotlinx.android.synthetic.main.fragment_dashboard_main.*

class DashboardFragment : Fragment() {

    protected lateinit var rootView: View
    private lateinit var eventAdapter: EventRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_dashboard_main, container, false)
        addRecyclerView()
        addDataSet()
        return rootView
    }

    private fun addRecyclerView() {
        rootView.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(activity)
            eventAdapter = EventRecyclerAdapter()
            adapter = eventAdapter
        }
    }

    private fun addDataSet() {
        val data = DataSource.createDataSet()
        eventAdapter.submitList(data)
    }

}