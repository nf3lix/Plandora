package com.plandora.controllers

import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.plandora.activity.CreateEventActivity
import com.plandora.activity.PlandoraActivity
import com.plandora.models.events.Event
import com.plandora.utils.constants.FirestoreConstants
import java.util.*
import kotlin.collections.ArrayList

class PlandoraEventController {

    companion object {
        val eventList: ArrayList<Event> = ArrayList()
    }

    private val firestoreInstance = FirebaseFirestore.getInstance()

    fun createEvent(activity: CreateEventActivity, event: Event) {
        firestoreInstance.collection(FirestoreConstants.EVENTS)
            .document()
            .set(event, SetOptions.merge())
            .addOnSuccessListener {
                activity.onSuccess()
                eventList.add(event)
            }
            .addOnFailureListener {
                activity.onFailure()
            }
    }

    fun getEventList(activity: PlandoraActivity) {
        val currentTimestamp = System.currentTimeMillis() - 8.64e7
        firestoreInstance.collection(FirestoreConstants.EVENTS)
            .whereEqualTo(FirestoreConstants.EVENT_OWNER_ID, PlandoraUserController().currentUserId())
            .get()
            .addOnSuccessListener { document ->
                eventList.clear()
                for(i in document.documents) {
                    val event = i.toObject(Event::class.java)!!
                    if(event.annual || event.timestamp > currentTimestamp) {
                        eventList.add(event)
                    }
                }
                eventList.sort()
            }
            .addOnFailureListener {
                Toast.makeText(activity.baseContext, it.message, Toast.LENGTH_SHORT).show()
            }
    }

}