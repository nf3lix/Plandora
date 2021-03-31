package com.plandora.controllers

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.plandora.activity.CreateEventActivity
import com.plandora.activity.PlandoraActivity
import com.plandora.models.events.Event
import com.plandora.models.gift_ideas.GiftIdea
import com.plandora.utils.constants.FirestoreConstants
import java.util.*
import java.util.Map
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PlandoraEventController {

    companion object {
        val eventList: ArrayList<Event> = ArrayList()
        val events: HashMap<String, Event> = HashMap();
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
                        events[i.id] = event
                    }
                }
                eventList.sort()
            }
            .addOnFailureListener {
                Toast.makeText(activity.baseContext, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    fun addEventGiftIdeas(oldEvent: Event, giftIdea: GiftIdea) {
        var id = ""
        for (entry: MutableMap.MutableEntry<String, Event> in events.entries) {
            if(entry.value == oldEvent) id = entry.key
        }
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(id)
            .update(FirestoreConstants.GIFT_IDEAS, FieldValue.arrayUnion(giftIdea))
            .addOnSuccessListener {
                Log.d("gi", "success fdsafdsfds")
            }
            .addOnFailureListener {
                Log.d("gi", "failure fdsafdsfds")
            }
    }

}