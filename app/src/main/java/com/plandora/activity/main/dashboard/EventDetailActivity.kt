package com.plandora.activity.main.dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.plandora.R
import com.plandora.activity.CreateEventActivity
import com.plandora.activity.PlandoraActivity
import com.plandora.activity.dialogs.AddGiftIdeaDialog
import com.plandora.activity.main.GiftIdeaDialogActivity
import com.plandora.adapters.AttendeeRecyclerAdapter
import com.plandora.adapters.GiftIdeaRecyclerAdapter
import com.plandora.controllers.PlandoraEventController
import com.plandora.controllers.PlandoraUserController
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import com.plandora.models.events.EventType
import com.plandora.models.gift_ideas.GiftIdea
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.app_bar_main.*

class EventDetailActivity : PlandoraActivity(),
    GiftIdeaDialogActivity,
    AttendeeRecyclerAdapter.OnDeleteButtonListener,
    GiftIdeaRecyclerAdapter.GiftIdeaClickListener {

    private lateinit var attendeesAdapter: AttendeeRecyclerAdapter
    private lateinit var giftIdeaAdapter: GiftIdeaRecyclerAdapter
    private var attendeesList: ArrayList<PlandoraUser> = ArrayList()
    private var giftIdeasList: ArrayList<GiftIdea> = ArrayList()

    private var eventId: String = "";
    private lateinit var oldEvent: Event;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        addActionBar()
        val event = intent.getParcelableExtra<Event>("event_object")!!
        oldEvent = event
        btn_add_gift_idea.setOnClickListener {
            AddGiftIdeaDialog(it.context, it.rootView as? ViewGroup, false, this).showDialog()
        }
        btn_delete_items.setOnClickListener {
            deleteAttendee()
        }
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

    override fun addGiftIdea(giftIdea: GiftIdea) {
        giftIdeasList.add(giftIdea)
        PlandoraEventController().addEventGiftIdeas(oldEvent, giftIdea)
    }

    override fun addGiftIdeasRecyclerView() {
        gift_ideas_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@EventDetailActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            giftIdeaAdapter = GiftIdeaRecyclerAdapter(giftIdeasList, this@EventDetailActivity)
            adapter = giftIdeaAdapter
        }
    }

    override fun onDeleteAttendeeButtonClicked(position: Int) {
    }

    override fun onGiftItemClicked(activated: Boolean) {
        btn_delete_items.visibility = if(activated) View.VISIBLE else View.GONE
    }

    override fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }

    private fun deleteAttendee() {
        val selectedItems = giftIdeaAdapter.getSelectedItems()
        for(i in 0 until selectedItems.size) {
            giftIdeasList.remove(selectedItems[i])
        }
        btn_delete_items.visibility = View.GONE
        addGiftIdeasRecyclerView()
    }

}