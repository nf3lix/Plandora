package com.plandora.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.plandora.R
import com.plandora.activity.components.date_time_picker.DatePickerObserver
import com.plandora.activity.components.date_time_picker.PlandoraDatePicker
import com.plandora.activity.components.date_time_picker.PlandoraTimePicker
import com.plandora.activity.components.date_time_picker.TimePickerObserver
import com.plandora.activity.components.dialogs.AddGiftIdeaDialog
import com.plandora.activity.components.dialogs.GiftIdeaDialog
import com.plandora.activity.main.GiftIdeaDialogActivity
import com.plandora.activity.main.dashboard.EventItemSpacingDecoration
import com.plandora.adapters.AttendeeRecyclerAdapter
import com.plandora.adapters.GiftIdeaRecyclerAdapter
import com.plandora.controllers.EventController
import com.plandora.controllers.State
import com.plandora.controllers.UserController
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import com.plandora.models.events.EventType
import com.plandora.models.gift_ideas.GiftIdea
import com.plandora.models.gift_ideas.GiftIdeaUIWrapper
import com.plandora.validator.Validator
import com.plandora.validator.validators.CreateEventValidator
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

open class CreateEventActivity :
    PlandoraActivity(),
    GiftIdeaDialogActivity,
    AttendeeRecyclerAdapter.OnDeleteButtonListener,
    GiftIdeaRecyclerAdapter.GiftIdeaClickListener,
    DatePickerObserver,
    TimePickerObserver
{

    private val uiScope = CoroutineScope(Dispatchers.Main)

    private lateinit var attendeesAdapter: AttendeeRecyclerAdapter
    private lateinit var giftIdeaAdapter: GiftIdeaRecyclerAdapter
    private var attendeesList: ArrayList<PlandoraUser> = ArrayList()
    private var giftIdeasList: ArrayList<GiftIdeaUIWrapper> = ArrayList()

    private lateinit var event: Event

    private var year = Calendar.getInstance().get(Calendar.YEAR)
    private var monthOfYear = Calendar.getInstance().get(Calendar.MONTH) + 1
    private var dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hours = 0; private var minutes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        attendees_linear_layout.visibility = View.GONE
        event = Event(ownerId = UserController().currentUserId(), attendees = arrayListOf())
        addAttendeesRecyclerView()
        addGiftIdeasRecyclerView()
        addActionBar()
        displaySelectedDate()
        displaySelectedTime()
        event_type_spinner.adapter = ArrayAdapter<EventType>(this, R.layout.support_simple_spinner_dropdown_item, EventType.values())
        setupButtonListeners()
    }

    private fun addAttendeesRecyclerView() {
        attendees_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@CreateEventActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            attendeesAdapter = AttendeeRecyclerAdapter(event, attendeesList, this@CreateEventActivity)
            adapter = attendeesAdapter
        }
    }

    override fun addGiftIdeasRecyclerView() {
        gift_ideas_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@CreateEventActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            giftIdeaAdapter = GiftIdeaRecyclerAdapter(giftIdeasList, this@CreateEventActivity)
            adapter = giftIdeaAdapter
        }
    }

    private fun deleteAttendee() {
        val selectedItems = giftIdeaAdapter.getSelectedItems()
        giftIdeasList.removeAll(selectedItems)
        btn_delete_items.visibility = View.GONE
        addGiftIdeasRecyclerView()
    }

    private fun selectDate() {
        PlandoraDatePicker(this, this).showDialog(year, monthOfYear - 1, dayOfMonth)
    }

    override fun updateSelectedDate(selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int) {
        year = selectedYear
        monthOfYear = selectedMonth + 1
        dayOfMonth = selectedDayOfMonth
        displaySelectedDate()
    }

    private fun selectTime() {
        PlandoraTimePicker(this, this).showDialog()
    }

    override fun updateSelectedTime(selectedHour: Int, selectedMinute: Int) {
        hours = selectedHour
        minutes = selectedMinute
        displaySelectedTime()
    }

    private fun displaySelectedDate() {
        event_date_input.setText(String.format(resources.getString(R.string.event_date_display),
            "%02d".format(monthOfYear), "%02d".format(dayOfMonth), "%04d".format(year)))
    }

    private fun displaySelectedTime() {
        event_time_input.setText(String.format(resources.getString(R.string.event_time_display),
            "%02d".format(hours), "%02d".format(minutes)))
    }

    override fun onDeleteAttendeeButtonClicked(position: Int) {
        attendeesList.remove(attendeesList[position])
        addAttendeesRecyclerView()
    }

    override fun onGiftItemClicked(position: Int) {
        GiftIdeaDialog(this, findViewById<ViewGroup>(android.R.id.content).rootView as ViewGroup, GiftIdeaUIWrapper.createGiftIdeaFromUIWrapper(giftIdeasList[position])).showDialog()
    }

    override fun onGiftIdeaSelected(position: Int) {
        btn_delete_items.visibility = View.VISIBLE
    }

    override fun onGiftIdeaDeselected(position: Int) {
        btn_delete_items.visibility = View.GONE
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
            timestamp = Event().getTimestamp(year, monthOfYear, dayOfMonth, hours, minutes)
            attendees = PlandoraUser().getIdsFromUserObjects(attendeesList)
            attendees.add(UserController().currentUserId())
            giftIdeas = list
        }
        validateForm(event)
        return true
    }

    private suspend fun createEvent(event: Event) {
        EventController().createEvent(event).collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> { finish() }
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
        Toast.makeText(this, state.validationMessage, Toast.LENGTH_SHORT).show()
        if(state.validationState == Validator.ValidationState.VALID) {
            uiScope.launch {
                createEvent(event)
            }
        }
    }

    private fun setupButtonListeners() {
        btn_date_picker.setOnClickListener { selectDate() }
        btn_time_picker.setOnClickListener { selectTime() }
        event_date_input.setOnClickListener { selectDate() }
        event_time_input.setOnClickListener { selectTime() }
        btn_add_gift_idea.setOnClickListener {
            AddGiftIdeaDialog(it.context, it.rootView as? ViewGroup, false, this).showDialog()
        }
        btn_delete_items.setOnClickListener {
            deleteAttendee()
        }
    }

}