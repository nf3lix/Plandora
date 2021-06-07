package com.plandora.activity.main.dashboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.plandora.R
import com.plandora.activity.EventActivity
import com.plandora.activity.components.date_time_picker.DatePickerObserver
import com.plandora.activity.components.date_time_picker.TimePickerObserver
import com.plandora.activity.components.dialogs.AddAttendeeDialog
import com.plandora.activity.components.dialogs.ConfirmDeletionDialog
import com.plandora.activity.components.dialogs.ConfirmDialogListener
import com.plandora.activity.main.GiftIdeaDialogActivity
import com.plandora.adapters.AttendeeRecyclerAdapter
import com.plandora.adapters.GiftIdeaRecyclerAdapter
import com.plandora.controllers.EventController
import com.plandora.controllers.InvitationController
import com.plandora.controllers.State
import com.plandora.controllers.UserController
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import com.plandora.models.events.EventChronology
import com.plandora.models.events.EventType
import com.plandora.models.gift_ideas.GiftIdea
import com.plandora.models.gift_ideas.GiftIdeaUIWrapper
import com.plandora.validator.validators.EditEventValidator
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EventDetailActivity : EventActivity(),
    GiftIdeaDialogActivity,
    AttendeeRecyclerAdapter.OnDeleteButtonListener,
    GiftIdeaRecyclerAdapter.GiftIdeaClickListener,
    ConfirmDialogListener,
    DatePickerObserver,
    TimePickerObserver
{

    private lateinit var newEvent: Event
    private val attendeesMap: HashMap<String, PlandoraUser> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActionBar()
        setupBasedOnEvent(event)
    }

    override fun initEvent() {
        event = intent.getParcelableExtra<Event>("event_object")!!
    }

    override fun initChrono() {
        eventChronology = EventChronology.eventChronologyFromEvent(event)
    }

    private fun setupBasedOnEvent(oldEvent: Event) {
        addBasicEventInformation(event)
        addGiftIdeasRecyclerView()
    }

    private fun addBasicEventInformation(event: Event) {
        event_title_input.setText(event.title)
        event_description_input.setText(event.description)
        event_date_input.setText(event.getDateAsString())
        event_time_input.setText(event.getTimeAsString())
        event_type_spinner.adapter = ArrayAdapter<EventType>(this, R.layout.support_simple_spinner_dropdown_item, EventType.values())
        event_type_spinner.setSelection(event.eventType.ordinal)
        cb_annual.isChecked = event.annual
        addAllAttendeesToList()
        addAllGiftIdeas(event.giftIdeas)
    }

    private fun addAllAttendeesToList() {
        uiScope.launch {
            for(userId in event.attendees) {
                addUserByIdToAttendeesList(userId)
            }
            for(userId in event.invitedUserIds) {
                addUserByIdToAttendeesList(userId)
            }
            addAttendeesRecyclerView()
        }
    }

    private suspend fun addUserByIdToAttendeesList(userId: String) {
        UserController().getUserById(userId).collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> {
                    attendeesList.add(state.data)
                    attendeesMap[userId] = state.data
                }
                is State.Failed -> {
                }
            }
        }
    }

    private fun addAllGiftIdeas(giftIdeas: ArrayList<GiftIdea>) {
        giftIdeas.forEach { giftIdea -> giftIdeasList.add(GiftIdeaUIWrapper.createFromGiftIdea(giftIdea)) }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        prepareDeleteIcon(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.save_entry -> {
                onSaveButtonClicked()
                true
            }
            R.id.delete_entry -> {
                ConfirmDeletionDialog(this, this).showDialog()
                true
            }
            else -> false
        }
    }

    private fun prepareDeleteIcon(menu: Menu?) {
        if (menu != null && !event.isOwner(UserController().currentUserId())) {
            menu.getItem(0).isVisible = false
        }
    }

    private fun onSaveButtonClicked() {
        newEvent = Event(
                event_title_input.text.toString(),
                EventType.valueOf(event_type_spinner.selectedItem.toString()),
                event_description_input.text.toString(),
                cb_annual.isChecked,
                eventChronology.getTimestamp(),
                event.attendees,
                event.giftIdeas
        )
        validateForm(newEvent)
    }

    private fun saveEntry() {
        uiScope.launch {
            updateEvent(event, newEvent)
        }
    }

    private suspend fun updateEvent(oldEvent: Event, newEvent: Event) {
        EventController().updateEvent(oldEvent, newEvent).collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> { finish() }
                is State.Failed -> { Toast.makeText(this, "Could not update event", Toast.LENGTH_SHORT).show() }
            }
        }
    }

    private suspend fun deleteEvent(event: Event) {
        showProgressBar()
        EventController().deleteEvent(event).collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> {
                    hideProgressBar()
                    finish()
                }
                is State.Failed -> {
                    hideProgressBar()
                    Toast.makeText(this@EventDetailActivity, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun addGiftIdea(giftIdea: GiftIdeaUIWrapper) {
        uiScope.launch {
            addGiftIdeaToEvent(event, GiftIdeaUIWrapper.createGiftIdeaFromUIWrapper(giftIdea))
        }
    }

    private suspend fun addGiftIdeaToEvent(event: Event, giftIdea: GiftIdea) {
        showProgressBar()
        EventController().addGiftIdeaToEvent(event, giftIdea).collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> {
                    hideProgressBar()
                    giftIdeasList.add(GiftIdeaUIWrapper.createFromGiftIdea(giftIdea))
                    addGiftIdeaToEventModel(giftIdea)
                }
                is State.Failed -> {
                    hideProgressBar()
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDeleteAttendeeButtonClicked(position: Int) {
        val attendee = attendeesList[position]
        uiScope.launch {
            if(!event.isInvitedUser(attendee)) {
                removeAttendee(attendee)
            } else {
                removePendingInvitation(attendee)
            }
        }
    }

    private suspend fun removeAttendee(attendee: PlandoraUser) {
        EventController().removeAttendee(attendee.id, event).collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> {
                    Toast.makeText(this, "User removed", Toast.LENGTH_LONG).show()
                    // event.invitedUserIds.remove(attendee.id)
                    attendeesList.remove(attendee)
                    attendeesAdapter.notifyDataSetChanged()
                }
                is State.Failed -> { }
            }
        }
    }

    private suspend fun removePendingInvitation(user: PlandoraUser) {
        var id = EventController().getEventId(event)
        if(id.isEmpty()) {
            event.invitedUserIds.remove(user.id)
            id = EventController().getEventId(event)
        }
        InvitationController().callBackInvitation(EventController().getEventId(event), user.id).collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> {
                    Toast.makeText(this, "User removed", Toast.LENGTH_LONG).show()
                    attendeesList.remove(user)
                    attendeesAdapter.notifyDataSetChanged()
                }
                is State.Failed -> { }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.event_detail_menu, menu)
        return true
    }

    override fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }

    private fun validateForm(event: Event) {
        val validation = EditEventValidator().getValidationState(event)
        if(validation.isInvalid()) {
            Toast.makeText(this, validation.validationMessage, Toast.LENGTH_SHORT).show()
            return
        }
        saveEntry()
    }

    private fun deleteSelectedEvents() {
        val selectedItems = giftIdeaAdapter.getSelectedItems()
        val giftIdeas = ArrayList<GiftIdea>()
        selectedItems.forEach { giftIdeas.add(GiftIdeaUIWrapper.createGiftIdeaFromUIWrapper(it)) }
        uiScope.launch {
            removeGiftIdeaFromEvent(event, giftIdeas[0])
        }
        btn_delete_items.visibility = View.GONE
    }

    private suspend fun removeGiftIdeaFromEvent(event: Event, giftIdea: GiftIdea) {
        EventController().removeGiftIdeaFromEvent(event, giftIdea).collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> {
                    giftIdeasList.remove(GiftIdeaUIWrapper.createFromGiftIdea(giftIdea, selected = true))
                    removeGiftIdeaFromEventModel(giftIdea)
                    addGiftIdeasRecyclerView()
                }
                is State.Failed -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private fun removeGiftIdeaFromEventModel(giftIdea: GiftIdea) {
        event.giftIdeas.remove(giftIdea)
    }

    private fun addGiftIdeaToEventModel(giftIdea: GiftIdea) {
        event.giftIdeas.add(giftIdea)
    }

    fun addAttendeeToList(attendee: PlandoraUser) {
        event.invitedUserIds.add(attendee.id)
        attendeesList.add(attendee)
        // event.invitedUserIds.remove(attendee.id)
    }

    override fun setupClickListeners() {
        super.setupClickListeners()
        btn_add_attendee.setOnClickListener {
            AddAttendeeDialog(it.context, it.rootView as? ViewGroup, false, event, this).showDialog()
        }
        btn_delete_items.setOnClickListener {
            deleteSelectedEvents()
        }
    }

    override fun onPositiveButtonClicked() {
        uiScope.launch {
            deleteEvent(event)
        }
    }

    private fun getUserId(user: PlandoraUser): String {
        var userId = ""
        for (entry: MutableMap.MutableEntry<String, PlandoraUser> in attendeesMap) {
            if(entry.value == user) {
                userId = entry.key
            }
        }
        return userId
    }

}