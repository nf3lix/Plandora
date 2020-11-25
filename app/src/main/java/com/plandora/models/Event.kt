package com.plandora.models

import android.os.Parcel
import android.os.Parcelable
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
    var attendees: ArrayList<PlandoraUser> = ArrayList(),
    var giftIdeas: ArrayList<GiftIdea> = ArrayList(),
    var ownerId: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        EventType.values()[parcel.readInt()],
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readLong(),
        parcel.createTypedArrayList(PlandoraUser.CREATOR)!!,
        parcel.createTypedArrayList(GiftIdea.CREATOR)!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeInt(eventType.ordinal)
        parcel.writeString(description)
        parcel.writeByte(if (annual) 1 else 0)
        parcel.writeLong(timestamp)
        parcel.writeTypedList(attendees)
        parcel.writeTypedList(giftIdeas)
        parcel.writeString(ownerId)
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
        val diff = timestamp - System.currentTimeMillis()
        return when(annual && diff < 0) {
            true -> millisToDays(getNextEvent(timestamp))
            else -> millisToDays(diff)
        }
    }

    fun isOwner(user: PlandoraUser): Boolean {
        return user.id.contentEquals(this.ownerId)
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

    fun getTimestamp(year: Int, monthOfYear: Int, dayOfMonth: Int, hours: Int, minutes: Int): Long {
        return SimpleDateFormat("dd-MM-yyyy mm:HH", Locale.US)
            .parse("${dayOfMonth.toString().format(2)}-${monthOfYear.toString().format(2)}-${year.toString().format(4)} ${hours.toString().format(2)}:${minutes.toString().format(2)}")!!.time
    }

}