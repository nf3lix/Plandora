package com.plandora.controllers

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.plandora.activity.CreateEventActivity
import com.plandora.activity.PlandoraActivity
import com.plandora.activity.main.dashboard.EventDetailActivity
import com.plandora.models.events.Event
import com.plandora.models.gift_ideas.GiftIdea
import com.plandora.utils.constants.FirestoreConstants
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
            .whereArrayContains(FirestoreConstants.ATTENDEES, PlandoraUserController().currentUserId())
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

    fun addEventGiftIdeas(activity: EventDetailActivity, oldEvent: Event, giftIdea: GiftIdea) {
        var id = ""
        for (entry: MutableMap.MutableEntry<String, Event> in events.entries) {
            if(entry.value == oldEvent) {
                id = entry.key
                if(entry.value.giftIdeas.contains(giftIdea)) {
                    Toast.makeText(activity.baseContext, "Diese Idee existiert bereits", Toast.LENGTH_SHORT).show();
                    return
                }
            }
        }

        if(id != "") {
            firestoreInstance.collection(FirestoreConstants.EVENTS).document(id)
                .update(FirestoreConstants.GIFT_IDEAS, FieldValue.arrayUnion(giftIdea))
                .addOnSuccessListener {
                    activity.giftIdeasList.add(giftIdea)
                    getEventList(activity)
                }
                .addOnFailureListener {
                    Toast.makeText(activity.baseContext, it.message, Toast.LENGTH_SHORT).show();
                }
        } else {
            Toast.makeText(activity.baseContext, "Fehler: Event konnte nicht mehr gefunden werden", Toast.LENGTH_SHORT).show();
        }
    }

}