package com.plandora.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.plandora.R
import com.plandora.activity.main.dashboard.EventItemSpacingDecoration
import com.plandora.adapters.AttendeeRecyclerAdapter
import com.plandora.adapters.GiftIdeaRecyclerAdapter
import com.plandora.models.Event
import com.plandora.models.EventType
import com.plandora.models.GiftIdea
import com.plandora.models.PlandoraUser
import kotlinx.android.synthetic.main.activity_new_event.*
import java.util.*
import kotlin.collections.ArrayList

class CreateEventActivity :
    AppCompatActivity(),
    AttendeeRecyclerAdapter.OnDeleteButtonListener,
    GiftIdeaRecyclerAdapter.GiftIdeaClickListener {

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
        btn_date_picker.setOnClickListener {
            selectDate()
        }

        btn_time_picker.setOnClickListener {
            selectTime()
        }

        event_date_input.setOnClickListener {
            selectDate()
        }

        event_time_input.setOnClickListener {
            selectTime()
        }

        event_type_spinner.adapter = ArrayAdapter<EventType>(this, R.layout.support_simple_spinner_dropdown_item, EventType.values())

        btn_add_attendee.setOnClickListener {
            val viewInflated: View = LayoutInflater
                .from(it.context)
                .inflate(R.layout.dialog_add_attendee, it.rootView as? ViewGroup, false)
            val b = AlertDialog.Builder(this)
                .setTitle("Add Attendee")
                .setView(viewInflated)
            // TODO: validate input
            b.setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
            b.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            b.show()
        }

        btn_add_gift_idea.setOnClickListener {
            val viewInflated: View = LayoutInflater
                .from(it.context)
                .inflate(R.layout.dialog_add_gift_idea, it.rootView as? ViewGroup, false)
            val b = AlertDialog.Builder(this)
                .setTitle("Add Gift Idea")
                .setView(viewInflated)
            // TODO: validate input
            b.setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
            b.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            b.show()
        }

        btn_delete_items.setOnClickListener {
            val selectedItems = giftIdeaAdapter.getSelectedItems()
            for(i in 0 until selectedItems.size) {
                giftIdeas.remove(selectedItems[i])
            }
            btn_delete_items.visibility = View.GONE
            addGiftIdeasRecyclerView()
        }

    }

    private fun addAttendeesRecyclerView() {
        attendees_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@CreateEventActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            attendeesAdapter = AttendeeRecyclerAdapter(event, attendees, this@CreateEventActivity)
            adapter = attendeesAdapter
        }
    }

    private fun addGiftIdeasRecyclerView() {
        gift_ideas_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@CreateEventActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            giftIdeaAdapter = GiftIdeaRecyclerAdapter(giftIdeas, this@CreateEventActivity)
            adapter = giftIdeaAdapter
        }
    }

    // TODO
    private fun loadUsers() {
        attendees = ArrayList()
        attendees.add(PlandoraUser("test", "test", "Felix", "test@test.de"))
        attendees.add(PlandoraUser("test1", "test", "Henry", "test@test.de"))
        attendees.add(PlandoraUser("test2", "test", "Vanessa", "test@test.de"))
    }

    // TODO
    private fun loadGiftIdeas() {
        val idea1 = GiftIdea("Geschenk1", "Beschreibung", "id1", 4.5F, ArrayList())
        val idea2 = GiftIdea("Geschenk1", "Beschreibung", "id1", 4.5F, ArrayList())
        val idea3 = GiftIdea("Geschenk1", "Beschreibung", "id1", 4.5F, ArrayList())
        giftIdeas = ArrayList()
        giftIdeas.add(idea1)
        giftIdeas.add(idea2)
        giftIdeas.add(idea3)
    }

    private fun selectDate() {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this@CreateEventActivity,
            R.style.SpinnerDatePickerStyle,
            { _, year, monthOfYear, dayOfMonth ->
                event_date_input.setText("${monthOfYear}-${dayOfMonth}-${year}")
            }, year, month, day)
        datePickerDialog.show()
    }

    private fun selectTime() {
        val timePickerDialog = TimePickerDialog(this@CreateEventActivity, {
                _, h, m ->
            event_time_input.setText("${h}:${m}")
        }, 0, 0, true)
        timePickerDialog.show()
    }

    override fun onButtonClickListener(position: Int) {
        attendees.remove(attendees[position])
        addAttendeesRecyclerView()
    }

    override fun onGiftItemClicked(activated: Boolean) {
        btn_delete_items.visibility = if(activated) View.VISIBLE else View.GONE
    }

}