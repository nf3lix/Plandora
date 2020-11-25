package com.plandora.models

import android.os.Parcel
import android.os.Parcelable
import com.plandora.controllers.PlandoraUserController

data class PlandoraUser(
    val id: String = "",
    val name: String = "",
    val displayName: String = "",
    val email: String = ""

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(displayName)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlandoraUser> {
        override fun createFromParcel(parcel: Parcel): PlandoraUser {
            return PlandoraUser(parcel)
        }

        override fun newArray(size: Int): Array<PlandoraUser?> {
            return arrayOfNulls(size)
        }

        fun createFromString(id: String): PlandoraUser {
            return PlandoraUserController().getUserFromId(id)
        }

    }

    fun getIdsFromUserObjects(userList: List<PlandoraUser>) : ArrayList<String> {
        val stringList: ArrayList<String> = ArrayList()
        userList.forEach { stringList.add(it.id) }
        return stringList
    }

}