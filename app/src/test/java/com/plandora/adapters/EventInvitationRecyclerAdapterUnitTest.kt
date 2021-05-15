package com.plandora.adapters

import com.plandora.activity.main.NotificationsFragment
import com.plandora.models.events.EventInvitation
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EventInvitationRecyclerAdapterUnitTest {

    private lateinit var adapter: EventInvitationRecyclerAdapter
    private var invitationList: ArrayList<EventInvitation> = ArrayList()
    private val activity = NotificationsFragment()

    @Before
    fun setUp() {
        adapter = EventInvitationRecyclerAdapter(invitationList, activity)
    }

    @Test
    fun getItemCount() {
        val invitation = EventInvitation()
        Assert.assertEquals(adapter.itemCount, 0)
        invitationList.add(EventInvitation())
        Assert.assertEquals(adapter.itemCount, 1)
        invitationList.add(invitation)
        Assert.assertEquals(adapter.itemCount, 2)
        invitationList.remove(invitation)
        Assert.assertEquals(adapter.itemCount, 1)
    }
}