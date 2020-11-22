package com.plandora.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.plandora.R
import com.plandora.account.Firestore
import com.plandora.account.PlandoraUserManager
import com.plandora.activity.dialogs.AddAttendeeDialog
import com.plandora.activity.dialogs.AddGiftIdeaDialog
import com.plandora.activity.main.dashboard.EventItemSpacingDecoration
import com.plandora.adapters.AttendeeRecyclerAdapter
import com.plandora.adapters.GiftIdeaRecyclerAdapter
import com.plandora.models.Event
import com.plandora.models.EventType
import com.plandora.models.GiftIdea
import com.plandora.models.PlandoraUser
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*
import kotlin.collections.ArrayList

class CreateEventActivity :
    PlandoraActivity(),
    AttendeeRecyclerAdapter.OnDeleteButtonListener,
    GiftIdeaRecyclerAdapter.GiftIdeaClickListener {

    private lateinit var attendeesAdapter: AttendeeRecyclerAdapter
    private lateinit var giftIdeaAdapter: GiftIdeaRecyclerAdapter
    private var attendeesList: ArrayList<PlandoraUser> = ArrayList()
    private var giftIdeasList: ArrayList<GiftIdea> = ArrayList()

    private lateinit var event: Event

    private var year = Calendar.getInstance().get(Calendar.YEAR)
    private var monthOfYear = Calendar.getInstance().get(Calendar.MONTH)
    private var dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hours = 0; private var minutes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        attendeesList.add(Firestore().getUserFromId(PlandoraUserManager().currentUserId()))
        event = Event(ownerId = PlandoraUserManager().currentUserId())

        addAttendeesRecyclerView()
        addGiftIdeasRecyclerView()
        addActionBar()

        displaySelectedDate()
        displaySelectedTime()

        btn_date_picker.setOnClickListener { selectDate() }
        btn_time_picker.setOnClickListener { selectTime() }
        event_date_input.setOnClickListener { selectDate() }
        event_time_input.setOnClickListener { selectTime() }

        event_type_spinner.adapter = ArrayAdapter<EventType>(this, R.layout.support_simple_spinner_dropdown_item, EventType.values())

        btn_add_attendee.setOnClickListener {
            AddAttendeeDialog(it.context, it.rootView as? ViewGroup, false, this).showDialog()
        }

        btn_add_gift_idea.setOnClickListener {
            AddGiftIdeaDialog(it.context, it.rootView as? ViewGroup, false, this).showDialog()
        }

        btn_delete_items.setOnClickListener {
            deleteAttendee()
        }

    }

    fun addAttendeesRecyclerView() {
        attendees_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@CreateEventActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            attendeesAdapter = AttendeeRecyclerAdapter(event, attendeesList, this@CreateEventActivity)
            adapter = attendeesAdapter
        }
    }

    fun addGiftIdeasRecyclerView() {
        gift_ideas_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@CreateEventActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            giftIdeaAdapter = GiftIdeaRecyclerAdapter(giftIdeasList, this@CreateEventActivity)
            adapter = giftIdeaAdapter
        }
    }

    private fun deleteAttendee() {
        val selectedItems = giftIdeaAdapter.getSelectedItems()
        for(i in 0 until selectedItems.size) {
            giftIdeasList.remove(selectedItems[i])
        }
        btn_delete_items.visibility = View.GONE
        addGiftIdeasRecyclerView()
    }

    private fun selectDate() {
        val datePickerDialog = DatePickerDialog(this@CreateEventActivity,
            R.style.SpinnerDatePickerStyle,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                year = selectedYear
                monthOfYear = selectedMonth
                dayOfMonth = selectedDayOfMonth
                displaySelectedDate()
            },  year, monthOfYear, dayOfMonth)
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
            R.id.save_entry -> {
                event.apply {
                    title = event_title_input.text.toString()
                    eventType = EventType.valueOf(event_type_spinner.selectedItem.toString())
                    description = event_description_input.toString()
                    annual = cb_annual.isSelected
                    timestamp = Event().getTimestamp(year, monthOfYear, dayOfMonth, hours, minutes)
                    attendees = attendeesList
                    giftIdeas = giftIdeasList
                }
                Firestore().createEvent(this, event)
                finish()
                true
            }
            R.id.close_creation -> {
                finish()
                true
            }
            else -> false
        }
    }

    override fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
    }

    fun addAttendee(attendee: PlandoraUser) {
        attendeesList.add(attendee)
    }

    fun addGiftIdea(giftIdea: GiftIdea) {
        giftIdeasList.add(giftIdea)
    }

}