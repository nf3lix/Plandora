package com.plandora.models.events

import android.os.Parcel
import android.os.Parcelable
import kotlin.math.roundToInt

data class EventInvitation(
    val invitedUserId: String,
    val invitationOwnerId: String,
    val eventId: String,
    val creationTimestamp: Long,
    val status: EventInvitationStatus = EventInvitationStatus.PENDING
) : Parcelable, Comparable<EventInvitation> {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        EventInvitationStatus.values()[parcel.readInt()]
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(invitedUserId)
        parcel.writeString(invitationOwnerId)
        parcel.writeString(eventId)
        parcel.writeLong(creationTimestamp)
        writeInt(status.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventInvitation> {
        override fun createFromParcel(parcel: Parcel): EventInvitation {
            return EventInvitation(parcel)
        }

        override fun newArray(size: Int): Array<EventInvitation?> {
            return arrayOfNulls(size)
        }
    }

    override fun compareTo(other: EventInvitation): Int {
        return millisToDays(creationTimestamp) - millisToDays(other.creationTimestamp)
    }

    private fun millisToDays(millis: Long): Int {
        return (millis / 864e5).roundToInt()
    }

}