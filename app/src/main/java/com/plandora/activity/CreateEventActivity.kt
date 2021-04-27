package com.plandora.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.plandora.R
import com.plandora.activity.dialogs.AddAttendeeDialog
import com.plandora.activity.dialogs.AddGiftIdeaDialog
import com.plandora.activity.main.GiftIdeaDialogActivity
import com.plandora.activity.main.dashboard.EventItemSpacingDecoration
import com.plandora.adapters.AttendeeRecyclerAdapter
import com.plandora.adapters.GiftIdeaRecyclerAdapter
import com.plandora.controllers.PlandoraEventController
import com.plandora.controllers.PlandoraUserController
import com.plandora.crud_workflows.CRUDActivity
import com.plandora.models.validation_types.CreateEventValidationTypes
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import com.plandora.models.events.EventType
import com.plandora.models.gift_ideas.GiftIdea
import com.plandora.models.gift_ideas.GiftIdeaUIWrapper
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*
import kotlin.collections.ArrayList

open class CreateEventActivity :
    PlandoraActivity(),
    GiftIdeaDialogActivity,
    AttendeeRecyclerAdapter.OnDeleteButtonListener,
    GiftIdeaRecyclerAdapter.GiftIdeaClickListener,
    CRUDActivity.EventCRUDActivity,
    CRUDActivity.InvitationCRUDActivity
{

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
        attendeesList.add(PlandoraUserController().getUserFromId(PlandoraUserController().currentUserId()))
        event = Event(ownerId = PlandoraUserController().currentUserId())
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
        val datePickerDialog = DatePickerDialog(this@CreateEventActivity,
            R.style.SpinnerDatePickerStyle,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                year = selectedYear
                monthOfYear = selectedMonth + 1
                dayOfMonth = selectedDayOfMonth
                displaySelectedDate()
            },  year, monthOfYear - 1, dayOfMonth)
        datePickerDialog.show()
    }

    private fun selectTime() {
        val timePickerDialog = TimePickerDialog(this@CreateEventActivity, {
                _, selectedHours, selectedMinutes ->
            hours = selectedHours
            minutes = selectedMinutes
            displaySelectedTime()
        }, 0, 0, true)
        timePickerDialog.show()
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

    override fun onGiftItemClicked(activated: Boolean) {
        btn_delete_items.visibility = if(activated) View.VISIBLE else View.GONE
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
            giftIdeas = list
        }
        val validation = validateForm(event)
        Toast.makeText(this, getString(validation.message), Toast.LENGTH_SHORT).show()
        if(validation == CreateEventValidationTypes.SUCCESS) {
            PlandoraEventController().createEvent(this, event)
        }
        return true
    }

    override fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }

    override fun onInvitationCreateSuccess(attendee: PlandoraUser) {
        Toast.makeText(this, "User successfully invited", Toast.LENGTH_LONG).show()
        attendeesList.add(attendee)
        addAttendeesRecyclerView()
    }

    override fun onInvitationCreateFailure() {
        onInternalFailure("Could not invite user")
    }

    override fun onInvitationExists() {
        onInternalFailure("This invitation already exists")
    }

    override fun addGiftIdea(giftIdea: GiftIdeaUIWrapper) {
        giftIdeasList.add(giftIdea)
    }

    private fun validateForm(event: Event): CreateEventValidationTypes {
        return when {
            !event.annual && event.timestamp < System.currentTimeMillis() -> { CreateEventValidationTypes.EVENT_IN_THE_PAST }
            event.title.isEmpty() -> { CreateEventValidationTypes.EMPTY_TITLE }
            else -> CreateEventValidationTypes.SUCCESS
        }
    }

    override fun onCreateSuccess(event: Event) {
        finish()
    }

    override fun onCreateFailure() {
        onInternalFailure("Could not create event")
    }

    override fun onUpdateSuccess(event: Event) {
        TODO("Not yet implemented")
    }

    override fun onUpdateFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onRemoveSuccess(event: Event) {
    }

    override fun onRemoveFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onInternalFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupButtonListeners() {
        btn_date_picker.setOnClickListener { selectDate() }
        btn_time_picker.setOnClickListener { selectTime() }
        event_date_input.setOnClickListener { selectDate() }
        event_time_input.setOnClickListener { selectTime() }
        btn_add_attendee.setOnClickListener {
            AddAttendeeDialog(it.context, it.rootView as? ViewGroup, false, event, this).showDialog()
        }
        btn_add_gift_idea.setOnClickListener {
            AddGiftIdeaDialog(it.context, it.rootView as? ViewGroup, false, this).showDialog()
        }
        btn_delete_items.setOnClickListener {
            deleteAttendee()
        }
    }

}