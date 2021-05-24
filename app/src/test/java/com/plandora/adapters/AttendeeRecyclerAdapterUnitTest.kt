package com.plandora.adapters

import com.plandora.activity.main.dashboard.EventDetailActivity
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class AttendeeRecyclerAdapterUnitTest {

    private lateinit var adapter: AttendeeRecyclerAdapter
    private var events = Event()
    private var itemList: ArrayList<PlandoraUser> = ArrayList()
    private val activity = EventDetailActivity()

    @Before
    fun setUp() {
        adapter = AttendeeRecyclerAdapter(events, itemList, activity)
    }

    @Test
    fun getItemCount() {
        val attendee = PlandoraUser()
        assertEquals(adapter.itemCount, 0)
        itemList.add(PlandoraUser())
        assertEquals(adapter.itemCount, 1)
        itemList.add(attendee)
        assertEquals(adapter.itemCount, 2)
        itemList.remove(attendee)
        assertEquals(adapter.itemCount, 1)
    }
}