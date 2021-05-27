package com.plandora.models.events

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.plandora.models.gift_ideas.GiftIdea
import com.plandora.models.PlandoraUser
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

data class Event(
    var title: String = "",
    var eventType: EventType = EventType.OTHERS,
    var description: String = "",
    var annual: Boolean = false,
    var timestamp: Long = 0,
    var attendees: ArrayList<String> = ArrayList(),
    var giftIdeas: ArrayList<GiftIdea> = ArrayList(),
    var ownerId: String = "",
    var invitedUserIds: ArrayList<String> = ArrayList()
) : Parcelable, Comparable<Event> {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        EventType.values()[parcel.readInt()],
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readLong(),
        parcel.createStringArrayList()!!,
        parcel.createTypedArrayList(GiftIdea)!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeString(title)
        writeInt(eventType.ordinal)
        writeString(description)
        writeByte(if (annual) 1 else 0)
        writeLong(timestamp)
        writeStringList(attendees)
        writeTypedList(giftIdeas)
        writeString(ownerId)
        writeStringList(invitedUserIds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }

    fun remainingDays(): Int {
        if(isToday()) {
            return 0
        }
        val diff = timestamp - System.currentTimeMillis()
        return when(annual && diff < 0) {
            true -> millisToDays(getNextEvent(timestamp))
            else -> millisToDays(diff)
        }
    }

    fun isOwner(user: PlandoraUser): Boolean {
        return user.id.contentEquals(this.ownerId)
    }

    fun isOwner(userId: String): Boolean {
        return userId.contentEquals(this.ownerId)
    }

    fun isInvitedUser(user: PlandoraUser): Boolean {
        return invitedUserIds.contains(user.id) && !attendees.contains(user.id)
    }

    private fun getNextEvent(timestamp: Long): Long {
        val date = Date(timestamp)
        val calendar = GregorianCalendar()
        calendar.apply {
            time = date
            set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) + 1)
        }
        return calendar.timeInMillis - System.currentTimeMillis()
    }

    private fun millisToDays(millis: Long): Int {
        return (millis / 864e5).roundToInt()
    }

    fun getDateAsString(): String {
        return SimpleDateFormat("MM-dd-yyyy").format(Timestamp(timestamp))
    }

    fun getTimeAsString(): String {
        return SimpleDateFormat("HH:mm").format(Timestamp(timestamp))
    }

    fun getTimestamp(year: Int, monthOfYear: Int, dayOfMonth: Int, hours: Int, minutes: Int): Long {
        return SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US)
            .parse("${dayOfMonth.toString().format(2)}-${monthOfYear.toString().format(2)}-${year.toString().format(4)} ${hours.toString().format(2)}:${minutes.toString().format(2)}")!!.time
    }

    fun relevantForDashboard(): Boolean {
        return !isInPast() || annual
    }

    private fun isInPast(): Boolean {
        val currentTimestamp = System.currentTimeMillis() - 8.64e7
        return timestamp < currentTimestamp
    }

    private fun isToday(): Boolean {
        val eventDate = SimpleDateFormat("MM-dd-yyyy").format(Timestamp(timestamp))
        val currentDate = SimpleDateFormat("MM-dd-yyyy").format(Timestamp(System.currentTimeMillis()))
        return eventDate.contentEquals(currentDate)
    }

    override fun compareTo(other: Event): Int {
        return remainingDays() - other.remainingDays()
    }

}