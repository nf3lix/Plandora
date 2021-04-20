package com.plandora.activity.main.dashboard

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.plandora.R
import com.plandora.activity.PlandoraActivity
import com.plandora.activity.dialogs.AddGiftIdeaDialog
import com.plandora.activity.main.GiftIdeaDialogActivity
import com.plandora.adapters.AttendeeRecyclerAdapter
import com.plandora.adapters.GiftIdeaRecyclerAdapter
import com.plandora.controllers.PlandoraEventController
import com.plandora.controllers.PlandoraUserController
import com.plandora.crud_workflows.CRUDActivity
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import com.plandora.models.events.EventType
import com.plandora.models.gift_ideas.GiftIdea
import com.plandora.models.gift_ideas.GiftIdeaUIWrapper
import com.plandora.models.validation_types.CreateEventValidationTypes
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.app_bar_main.*

class EventDetailActivity : PlandoraActivity(),
    GiftIdeaDialogActivity,
    AttendeeRecyclerAdapter.OnDeleteButtonListener,
    GiftIdeaRecyclerAdapter.GiftIdeaClickListener,
    CRUDActivity.EventCRUDActivity,
    CRUDActivity.GiftIdeaCRUDActivity
{

    private lateinit var attendeesAdapter: AttendeeRecyclerAdapter
    private lateinit var giftIdeaAdapter: GiftIdeaRecyclerAdapter
    private val attendeesList: ArrayList<PlandoraUser> = ArrayList()
    val giftIdeasList: ArrayList<GiftIdeaUIWrapper> = ArrayList()

    private lateinit var oldEvent: Event

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
            deleteSelectedEvents()
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
            giftIdeasList.add(GiftIdeaUIWrapper.createFromGiftIdea(giftIdea))
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.save_entry -> {
                saveEntry()
                true
            }
            else -> false
        }
    }

    private fun saveEntry() {
        // irgendwas geändert?
        PlandoraEventController().updateEvent(this, oldEvent)
    }

    override fun addGiftIdea(giftIdea: GiftIdeaUIWrapper) {
        PlandoraEventController().addEventGiftIdea(this, oldEvent, GiftIdeaUIWrapper.createGiftIdeaFromUIWrapper(giftIdea))
    }

    override fun addGiftIdeasRecyclerView() {
        gift_ideas_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@EventDetailActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            giftIdeaAdapter = GiftIdeaRecyclerAdapter(
                    giftIdeasList, this@EventDetailActivity, false)
            adapter = giftIdeaAdapter
        }
    }

    override fun onDeleteAttendeeButtonClicked(position: Int) {
    }

    override fun onGiftItemClicked(activated: Boolean) {
        btn_delete_items.visibility = if(giftIdeaAdapter.getSelectedItems().size > 0) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.event_detail_menu, menu)
        return true
    }

    override fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }

    private fun deleteSelectedEvents() {
        val selectedItems = giftIdeaAdapter.getSelectedItems()
        val giftIdeas = ArrayList<GiftIdea>()
        selectedItems.forEach {
            giftIdeas.add(GiftIdeaUIWrapper.createGiftIdeaFromUIWrapper(it))
        }
        PlandoraEventController().removeEventGiftIdea(this, oldEvent, giftIdeas[0])
        btn_delete_items.visibility = View.GONE
    }

    fun removeGiftIdeaFromEventModel(giftIdea: GiftIdea) {
        oldEvent.giftIdeas.remove(giftIdea)
    }

    fun addGiftIdeaToEventModel(giftIdea: GiftIdea) {
        oldEvent.giftIdeas.add(giftIdea)
    }

    override fun onCreateSuccess(giftIdea: GiftIdea) {
        giftIdeasList.add(GiftIdeaUIWrapper.createFromGiftIdea(giftIdea))
        addGiftIdeaToEventModel(giftIdea)
    }

    override fun onCreateSuccess(event: Event) {
        TODO("Not yet implemented")
    }

    override fun onCreateFailure() {
        TODO("Not yet implemented")
    }

    override fun onUpdateSuccess(event: Event) {
        TODO("Not yet implemented")
    }

    override fun onUpdateFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onRemoveSuccess(event: Event) {
        TODO("Not yet implemented")
    }

    override fun onRemoveSuccess(giftIdea: GiftIdea) {
        giftIdeasList.remove(GiftIdeaUIWrapper.createFromGiftIdea(giftIdea, selected = true))
        removeGiftIdeaFromEventModel(giftIdea)
        addGiftIdeasRecyclerView()
    }

    override fun onRemoveFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    override fun onInternalFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}