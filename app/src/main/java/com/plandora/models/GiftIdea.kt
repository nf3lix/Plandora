package com.plandora.models

import android.os.Parcel
import android.os.Parcelable

data class GiftIdea(
    val title: String = "",
    val description: String = "",
    val ownerId: String = "",
    val rating: Float = 0F,
    val votes: ArrayList<PlandoraUser> = ArrayList()
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.createTypedArrayList(PlandoraUser.CREATOR)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(ownerId)
        parcel.writeFloat(rating)
        parcel.writeTypedList(votes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GiftIdea> {
        override fun createFromParcel(parcel: Parcel): GiftIdea {
            return GiftIdea(parcel)
        }

        override fun newArray(size: Int): Array<GiftIdea?> {
            return arrayOfNulls(size)
        }
    }
}