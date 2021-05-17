package com.plandora.adapters

import com.plandora.activity.main.dashboard.DashboardFragment
import com.plandora.models.events.Event
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class EventRecyclerAdapterUnitTest {

    private lateinit var adapter: EventRecyclerAdapter
    private var itemList: ArrayList<Event> = ArrayList()
    private val activity = DashboardFragment()

    @Before
    fun setUp() {
        adapter = EventRecyclerAdapter(itemList, activity)
    }

    @Test
    fun getItemCount() {
        val event = Event()
        assertEquals(adapter.itemCount, 0)
        itemList.add(Event())
        assertEquals(adapter.itemCount, 1)
        itemList.add(event)
        assertEquals(adapter.itemCount, 2)
        itemList.remove(event)
        assertEquals(adapter.itemCount, 1)
    }
}