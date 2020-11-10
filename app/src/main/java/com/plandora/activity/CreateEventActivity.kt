package com.plandora.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.plandora.R
import com.plandora.activity.main.dashboard.EventItemSpacingDecoration
import com.plandora.adapters.AttendeeRecyclerAdapter
import com.plandora.adapters.GiftIdeaRecyclerAdapter
import com.plandora.models.Event
import com.plandora.models.GiftIdea
import com.plandora.models.PlandoraUser
import kotlinx.android.synthetic.main.activity_new_event.*

class CreateEventActivity : AppCompatActivity() {

    private lateinit var attendeesAdapter: AttendeeRecyclerAdapter
    private lateinit var attendees: ArrayList<PlandoraUser>
    private lateinit var event: Event
    private lateinit var giftIdeaAdapter: GiftIdeaRecyclerAdapter
    private lateinit var giftIdeas: ArrayList<GiftIdea>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event)
        loadUsers()
        event = Event(ownerId = attendees[0].id)
        addAttendeesRecyclerView()
        loadGiftIdeas()
        addGiftIdeasRecyclerView()
    }

    private fun addAttendeesRecyclerView() {
        attendees_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@CreateEventActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            attendeesAdapter = AttendeeRecyclerAdapter(event, attendees)
            adapter = attendeesAdapter
        }
    }

    private fun addGiftIdeasRecyclerView() {
        gift_ideas_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@CreateEventActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            giftIdeaAdapter = GiftIdeaRecyclerAdapter(giftIdeas)
            adapter = giftIdeaAdapter
        }
    }

    // TODO
    private fun loadUsers() {
        val owner = PlandoraUser("test", "test", "Felix", "test@test.de")
        val user1 = PlandoraUser("test1", "test", "Henry", "test@test.de")
        val user2 = PlandoraUser("test2", "test", "Vanessa", "test@test.de")
        attendees = ArrayList()
        attendees.add(owner)
        attendees.add(user1)
        attendees.add(user2)
    }

    // TODO
    private fun loadGiftIdeas() {
        //val idea1 = GiftIdea("Geschenk1", "Beschreibung", "id1", 4.5F, ArrayList())
        //val idea2 = GiftIdea("Geschenk1", "Beschreibung", "id1", 4.5F, ArrayList())
        //val idea3 = GiftIdea("Geschenk1", "Beschreibung", "id1", 4.5F, ArrayList())
        giftIdeas = ArrayList()
        //giftIdeas.add(idea1)
        //giftIdeas.add(idea2)
        //giftIdeas.add(idea3)
    }

}