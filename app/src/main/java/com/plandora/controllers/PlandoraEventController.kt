package com.plandora.controllers

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.plandora.activity.CreateEventActivity
import com.plandora.activity.PlandoraActivity
import com.plandora.activity.main.MainActivity
import com.plandora.activity.main.dashboard.DashboardFragment
import com.plandora.models.Event
import com.plandora.utils.constants.FirestoreConstants

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
            }
            .addOnFailureListener {
                activity.onFailure()
            }
    }

    fun getEventList(activity: PlandoraActivity) {
        firestoreInstance.collection(FirestoreConstants.EVENTS)
            .whereEqualTo(FirestoreConstants.EVENT_OWNER_ID, PlandoraUserController().currentUserId())
            .get()
            .addOnSuccessListener { document ->
                eventList.clear()
                for(i in document.documents) {
                    val event = i.toObject(Event::class.java)!!
                    eventList.add(event)
                }
            }
            .addOnFailureListener {
                Toast.makeText(activity.baseContext, "Could not load events", Toast.LENGTH_SHORT).show()
            }
    }

}