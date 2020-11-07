package com.plandora.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.plandora.R
import com.plandora.activity.main.dashboard.EventItemSpacingDecoration
import com.plandora.models.AttendeeRecyclerAdapter
import com.plandora.models.Event
import com.plandora.models.PlandoraUser
import kotlinx.android.synthetic.main.activity_new_event.*

class CreateEventActivity : AppCompatActivity() {

    private lateinit var attendeesAdapter: AttendeeRecyclerAdapter
    private lateinit var attendees: ArrayList<PlandoraUser>
    private lateinit var event: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event)
        val owner = PlandoraUser("test", "test", "Felix", "test@test.de")
        val user1 = PlandoraUser("test1", "test", "Henry", "test@test.de")
        val user2 = PlandoraUser("test2", "test", "Vanessa", "test@test.de")
        attendees = ArrayList()
        attendees.add(owner)
        attendees.add(user1)
        attendees.add(user2)
        event = Event(ownerId = owner.id)
        addAttendeesRecyclerView()
    }

    private fun addAttendeesRecyclerView() {
        attendees_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@CreateEventActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            attendeesAdapter = AttendeeRecyclerAdapter(event, attendees)
            adapter = attendeesAdapter
        }
    }

}