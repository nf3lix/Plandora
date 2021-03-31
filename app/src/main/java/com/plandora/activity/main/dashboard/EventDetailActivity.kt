package com.plandora.activity.main.dashboard

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.plandora.R
import com.plandora.activity.PlandoraActivity
import com.plandora.adapters.AttendeeRecyclerAdapter
import com.plandora.adapters.GiftIdeaRecyclerAdapter
import com.plandora.controllers.PlandoraUserController
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import com.plandora.models.events.EventType
import com.plandora.models.gift_ideas.GiftIdea
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.app_bar_main.*

class EventDetailActivity : PlandoraActivity(),
    AttendeeRecyclerAdapter.OnDeleteButtonListener,
    GiftIdeaRecyclerAdapter.GiftIdeaClickListener {

    private lateinit var attendeesAdapter: AttendeeRecyclerAdapter
    private lateinit var giftIdeaAdapter: GiftIdeaRecyclerAdapter
    private var attendeesList: ArrayList<PlandoraUser> = ArrayList()
    private var giftIdeasList: ArrayList<GiftIdea> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        addActionBar()
        val event = intent.getParcelableExtra<Event>("event_object")!!
        addEventInformation(event)
        addAttendeesRecyclerView(event)
        addGiftIdeasRecyclerView()
    }

    private fun addEventInformation(event: Event) {
        event_title_input.setText(event.title)
        event_description_input.setText(event.description)
        event_date_input.setText(event.getDateAsString())
        event_time_input.setText(event.getTimeAsString())
        event_type_spinner.adapter = ArrayAdapter<EventType>(this, R.layout.support_simple_spinner_dropdown_item, EventType.values())
        event_type_spinner.setSelection(event.eventType.ordinal)
        cb_annual.isChecked = event.annual
        for(userId in event.attendees) {
            attendeesList.add(PlandoraUserController().getUserFromId(userId))
        }
        for(giftIdea in event.giftIdeas) {
            giftIdeasList.add(giftIdea)
        }
    }

    private fun addAttendeesRecyclerView(event: Event) {
        attendees_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@EventDetailActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            attendeesAdapter = AttendeeRecyclerAdapter(event, attendeesList, this@EventDetailActivity)
            adapter = attendeesAdapter
        }
    }

    private fun addGiftIdeasRecyclerView() {
        gift_ideas_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@EventDetailActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            giftIdeaAdapter = GiftIdeaRecyclerAdapter(giftIdeasList, this@EventDetailActivity)
            adapter = giftIdeaAdapter
        }
    }

    override fun onDeleteAttendeeButtonClicked(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onGiftItemClicked(activated: Boolean) {
        TODO("Not yet implemented")
    }

    override fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }

}