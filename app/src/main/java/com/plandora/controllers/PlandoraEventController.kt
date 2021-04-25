package com.plandora.controllers

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.plandora.crud_workflows.CRUDActivity
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import com.plandora.models.events.EventInvitation
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

    fun createEvent(activity: CRUDActivity.EventCRUDActivity, event: Event) {
        firestoreInstance.collection(FirestoreConstants.EVENTS)
            .document()
            .set(event, SetOptions.merge())
            .addOnSuccessListener {
                activity.onCreateSuccess(event)
                eventList.add(event)
            }
            .addOnFailureListener {
                activity.onCreateFailure()
            }
    }

    fun getEventList(activity: CRUDActivity) {
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
                activity.onInternalFailure(it.message!!)
            }
    }

    fun updateEvent(activity: CRUDActivity.EventCRUDActivity, oldEvent: Event, event: Event) {
        var id = ""
        for (entry: MutableMap.MutableEntry<String, Event> in events.entries) {
            if(entry.value == oldEvent) {
                id = entry.key
            }
        }

        if(id != "") {
            firestoreInstance.collection(FirestoreConstants.EVENTS)
                .document(id)
                .update(
                    FirestoreConstants.EVENT_TITLE, event.title,
                    FirestoreConstants.EVENT_DESCRIPTION, event.description,
                    FirestoreConstants.EVENT_ANNUAL, event.annual,
                    FirestoreConstants.EVENT_DATE_AS_STRING, event.getDateAsString(),
                    FirestoreConstants.EVENT_TYPE, event.eventType,
                    FirestoreConstants.EVENT_TIME_AS_STRING, event.getTimeAsString(),
                    FirestoreConstants.EVENT_TIMESTAMP, event.timestamp
                )
                .addOnSuccessListener {
                    Log.d("edit_event", "successfully edited event")
                }
                .addOnFailureListener {
                    Log.d("edit_event", "could not edit event")
                }
        } else {
            activity.onUpdateFailure("Error: Event could not be found")
        }

    }

    fun addEventGiftIdea(activity: CRUDActivity.GiftIdeaCRUDActivity, oldEvent: Event, giftIdea: GiftIdea) {
        var id = ""
        for (entry: MutableMap.MutableEntry<String, Event> in events.entries) {
            if(entry.value == oldEvent) {
                id = entry.key
                if(entry.value.giftIdeas.contains(giftIdea)) {
                    activity.onInternalFailure("Diese Idee existiert bereits")
                    return
                }
            }
        }

        if(id != "") {
            firestoreInstance.collection(FirestoreConstants.EVENTS).document(id)
                .update(FirestoreConstants.GIFT_IDEAS, FieldValue.arrayUnion(giftIdea))
                .addOnSuccessListener {
                    events[id]?.giftIdeas?.add(giftIdea)
                    activity.onCreateSuccess(giftIdea)
                    getEventList(activity)
                }
                .addOnFailureListener { activity.onCreateFailure() }
        } else {
            activity.onInternalFailure("Failure: Event could not be found")
        }
    }

    fun removeEventGiftIdea(activity: CRUDActivity.GiftIdeaCRUDActivity, oldEvent: Event, giftIdea: GiftIdea) {
        var id = ""
        for (entry: MutableMap.MutableEntry<String, Event> in events.entries) {
            if(entry.value == oldEvent) {
                id = entry.key
            }
        }

        if(id != "") {
            firestoreInstance.collection(FirestoreConstants.EVENTS).document(id)
                    .update(FirestoreConstants.GIFT_IDEAS, FieldValue.arrayRemove(giftIdea))
                    .addOnSuccessListener {
                        events[id]?.giftIdeas?.remove(giftIdea)
                        activity.onRemoveSuccess(giftIdea)
                    }
                    .addOnFailureListener {
                        activity.onRemoveFailure(it.message!!)
            }
        } else {
            activity.onInternalFailure("Failure: Event could not be found")
        }
    }

    fun createEventInvitation(event: Event, invitedUser: PlandoraUser, activity: CRUDActivity.InvitationCRUDActivity) {
        var id = ""
        for (entry: MutableMap.MutableEntry<String, Event> in events.entries) {
            if(entry.value == event) {
                id = entry.key
            }
        }

        if(id != "") {
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(id).get()
                .addOnSuccessListener {
                    val resolvedEvent = it.toObject(Event::class.java)!!
                    if(!resolvedEvent.invitedUserIds.contains(invitedUser.id)) {
                        val invitation = EventInvitation(PlandoraUserController().currentUserId(), invitedUser.id, id, System.currentTimeMillis())
                        firestoreInstance.collection(FirestoreConstants.INVITATIONS)
                                .document()
                                .set(invitation, SetOptions.merge())
                                .addOnSuccessListener {
                                    firestoreInstance.collection(FirestoreConstants.EVENTS).document(id)
                                            .update("invitedUserIds", FieldValue.arrayUnion(invitedUser.id))
                                    activity.onInvitationCreateSuccess(invitedUser)
                                }
                                .addOnFailureListener {
                                    activity.onInvitationCreateFailure()
                                }
                    } else {
                        activity.onInvitationExists()
                    }
                }
        } else {
            activity.onInternalFailure("Failure: Event could not be found")
        }
    }

}