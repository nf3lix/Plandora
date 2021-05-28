package com.plandora.activity

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.plandora.R
import com.plandora.activity.components.date_time_picker.DatePickerObserver
import com.plandora.activity.components.date_time_picker.PlandoraDatePicker
import com.plandora.activity.components.date_time_picker.PlandoraTimePicker
import com.plandora.activity.components.date_time_picker.TimePickerObserver
import com.plandora.activity.components.dialogs.GiftIdeaDialog
import com.plandora.activity.main.GiftIdeaDialogActivity
import com.plandora.activity.main.dashboard.EventItemSpacingDecoration
import com.plandora.adapters.AttendeeRecyclerAdapter
import com.plandora.adapters.GiftIdeaRecyclerAdapter
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import com.plandora.models.gift_ideas.GiftIdeaUIWrapper
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class EventActivity :
    PlandoraActivity(),
    GiftIdeaRecyclerAdapter.GiftIdeaClickListener,
    DatePickerObserver, TimePickerObserver,
    GiftIdeaDialogActivity,
    AttendeeRecyclerAdapter.OnDeleteButtonListener
{

    internal lateinit var event: Event

    internal lateinit var attendeesAdapter: AttendeeRecyclerAdapter
    internal lateinit var giftIdeaAdapter: GiftIdeaRecyclerAdapter
    internal val attendeesList: ArrayList<PlandoraUser> = ArrayList()
    internal val giftIdeasList: ArrayList<GiftIdeaUIWrapper> = ArrayList()

    internal var year = 0
    internal var monthOfYear = 0
    internal var dayOfMonth = 0
    internal var hours = 0;
    internal var minutes = 0

    internal val uiScope = CoroutineScope(Dispatchers.Main)

    internal abstract fun initChrono()

    internal fun displaySelectedDate() {
        event_date_input.setText(String.format(resources.getString(R.string.event_date_display),
            "%02d".format(monthOfYear), "%02d".format(dayOfMonth), "%04d".format(year)))
    }

    internal fun displaySelectedTime() {
        event_time_input.setText(String.format(resources.getString(R.string.event_time_display),
            "%02d".format(hours), "%02d".format(minutes)))
    }

    internal fun selectDate() {
        PlandoraDatePicker(this, this).showDialog(year, monthOfYear - 1, dayOfMonth)
    }

    internal fun selectTime() {
        PlandoraTimePicker(this, this).showDialog()
    }

    override fun onGiftItemClicked(position: Int) {
        GiftIdeaDialog(this, findViewById<ViewGroup>(android.R.id.content).rootView as ViewGroup, GiftIdeaUIWrapper.createGiftIdeaFromUIWrapper(giftIdeasList[position])).showDialog()
    }

    override fun onGiftIdeaSelected(position: Int) {
        Handler().postDelayed({
            btn_delete_items.visibility = View.VISIBLE
        }, 20)
    }

    override fun onGiftIdeaDeselected(position: Int) {
        Handler().postDelayed({
            btn_delete_items.visibility = View.GONE
        }, 20)
    }

    override fun updateSelectedDate(selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int) {
        year = selectedYear
        monthOfYear = selectedMonth + 1
        dayOfMonth = selectedDayOfMonth
        displaySelectedDate()
    }

    override fun updateSelectedTime(selectedHour: Int, selectedMinute: Int) {
        hours = selectedHour
        minutes = selectedMinute
        displaySelectedTime()
    }

    override fun addGiftIdeasRecyclerView() {
        gift_ideas_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@EventActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            giftIdeaAdapter = GiftIdeaRecyclerAdapter(
                giftIdeasList, this@EventActivity)
            adapter = giftIdeaAdapter
        }
    }

    private fun addAttendeesRecyclerView() {
        attendees_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@EventActivity)
            addItemDecoration(EventItemSpacingDecoration(5))
            attendeesAdapter = AttendeeRecyclerAdapter(event, attendeesList, this@EventActivity)
            adapter = attendeesAdapter
        }
    }

    override fun onDeleteAttendeeButtonClicked(position: Int) {
        attendeesList.remove(attendeesList[position])
        addAttendeesRecyclerView()
    }

}
