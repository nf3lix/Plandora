package com.plandora.activity

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.plandora.R
import com.plandora.activity.components.date_time_picker.DatePickerObserver
import com.plandora.activity.components.date_time_picker.TimePickerObserver
import com.plandora.activity.main.GiftIdeaDialogActivity
import com.plandora.adapters.AttendeeRecyclerAdapter
import com.plandora.adapters.GiftIdeaRecyclerAdapter
import com.plandora.controllers.EventController
import com.plandora.controllers.State
import com.plandora.controllers.UserController
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import com.plandora.models.events.EventChronology
import com.plandora.models.events.EventType
import com.plandora.models.gift_ideas.GiftIdea
import com.plandora.models.gift_ideas.GiftIdeaUIWrapper
import com.plandora.validator.Validator
import com.plandora.validator.validators.CreateEventValidator
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

open class CreateEventActivity :
    EventActivity(),
    GiftIdeaDialogActivity,
    AttendeeRecyclerAdapter.OnDeleteButtonListener,
    GiftIdeaRecyclerAdapter.GiftIdeaClickListener,
    DatePickerObserver,
    TimePickerObserver
{

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val networkCheck = NetworkCheck(this)
        networkCheck.registerNetworkCallback()
        attendees_linear_layout.visibility = View.GONE
        addAttendeesRecyclerView()
        addGiftIdeasRecyclerView()
        addActionBar()
        displaySelectedDate()
        displaySelectedTime()
        event_type_spinner.adapter = ArrayAdapter<EventType>(this, R.layout.support_simple_spinner_dropdown_item, EventType.values())
    }

    override fun initEvent() {
        event = Event(ownerId = UserController().currentUserId(), attendees = arrayListOf())
    }

    override fun initChrono() {
        eventChronology = EventChronology.eventChronologyFromCurrentTimestamp()
    }

    private fun deleteAttendee() {
        val selectedItems = giftIdeaAdapter.getSelectedItems()
        giftIdeasList.removeAll(selectedItems)
        btn_delete_items.visibility = View.GONE
        addGiftIdeasRecyclerView()
    }

    override fun onDeleteAttendeeButtonClicked(position: Int) {
        attendeesList.remove(attendeesList[position])
        addAttendeesRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_event_tool_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.save_entry -> { saveNewEntry() }
            R.id.close_creation -> {
                finish()
                true
            }
            else -> false
        }
    }

    private fun saveNewEntry(): Boolean {
        val list = ArrayList<GiftIdea>()
        giftIdeasList.forEach {
            list.add(GiftIdeaUIWrapper.createGiftIdeaFromUIWrapper(it))
        }
        event.apply {
            title = event_title_input.text.toString()
            eventType = EventType.valueOf(event_type_spinner.selectedItem.toString())
            description = event_description_input.text.toString()
            annual = cb_annual.isChecked
            timestamp = eventChronology.getTimestamp()
            attendees = PlandoraUser().getIdsFromUserObjects(attendeesList)
            attendees.add(UserController().currentUserId())
            giftIdeas = list
        }
        validateForm(event)
        return true
    }

    private suspend fun createEvent(event: Event) {
        if(!NetworkCheck.isNetworkConnected) {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG).show()
            return
        }
        showProgressBar()
        EventController().createEvent(event).collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> {
                    hideProgressBar()
                    finish() }
                is State.Failed -> { Toast.makeText(this, "Could not create event", Toast.LENGTH_LONG).show() }
            }
        }
    }

    override fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }

    override fun addGiftIdea(giftIdea: GiftIdeaUIWrapper) {
        giftIdeasList.add(giftIdea)
    }

    private fun validateForm(event: Event) {
        val state = CreateEventValidator().getValidationState(event)
        when(state.validationState) {
            Validator.ValidationState.INVALID -> {
                Toast.makeText(this, state.validationMessage, Toast.LENGTH_SHORT).show()
            }
            Validator.ValidationState.VALID -> {
                uiScope.launch {
                    createEvent(event)
                }
            }
            else -> Toast.makeText(this, state.validationMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun setupClickListeners() {
        super.setupClickListeners()
        btn_delete_items.setOnClickListener {
            deleteAttendee()
        }
    }

}