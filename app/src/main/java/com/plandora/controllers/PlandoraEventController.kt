package com.plandora.controllers

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.plandora.crud_workflows.CRUDActivity
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import com.plandora.models.events.EventInvitation
import com.plandora.models.gift_ideas.GiftIdea
import com.plandora.utils.constants.FirestoreConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PlandoraEventController {

    companion object {
        val eventList: ArrayList<Event> = ArrayList()
        val events: HashMap<String, Event> = HashMap();
    }

    private val firestoreInstance = FirebaseFirestore.getInstance()

    fun createEvent(event: Event) = flow<State<String>> {
        emit(State.loading())
        firestoreInstance.collection(FirestoreConstants.EVENTS)
            .document()
            .set(event, SetOptions.merge()).await()
        eventList.add(event)
        emit(State.success(""))
    }.catch {
        emit(State.failed(it.message.toString()))
    }

    fun updateEventList() = flow<State<String>> {
        emit(State.loading())
        val document = firestoreInstance.collection(FirestoreConstants.EVENTS)
            .whereArrayContains(FirestoreConstants.ATTENDEES, PlandoraUserController().currentUserId())
            .get().await()
        transformEventListDocument(document)
        emit(State.success(""))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private fun transformEventListDocument(querySnapshot: QuerySnapshot) {
        eventList.clear()
        addEventsFromQuerySnapshot(querySnapshot)
        eventList.sort()
    }

    private fun addEventsFromQuerySnapshot(querySnapshot: QuerySnapshot) {
        querySnapshot.forEach { document ->
            val event = document.toObject(Event::class.java)
            if(event.relevantForDashboard()) {
                eventList.add(event)
                events[document.id] = event
            }
        }
    }

    fun updateEvent(activity: CRUDActivity.EventCRUDActivity, oldEvent: Event, event: Event) {
        val id = getEventId(oldEvent)
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
                    activity.onUpdateSuccess(event)
                }
                .addOnFailureListener {
                    Log.d("edit_event", "could not edit event")
                }
        } else {
            activity.onUpdateFailure("Error: Event could not be found")
        }
    }

    fun updateEvent(oldEvent: Event, newEvent: Event) = flow<State<String>> {
        emit(State.loading())
        val id = getEventId(oldEvent)
        if(id.isEmpty()) {
            emit(State.failed("Error: Event could not be found"))
            return@flow
        }
        firestoreInstance.collection(FirestoreConstants.EVENTS)
                .document(id)
                .update(
                    FirestoreConstants.EVENT_TITLE, newEvent.title,
                    FirestoreConstants.EVENT_DESCRIPTION, newEvent.description,
                    FirestoreConstants.EVENT_ANNUAL, newEvent.annual,
                    FirestoreConstants.EVENT_DATE_AS_STRING, newEvent.getDateAsString(),
                    FirestoreConstants.EVENT_TYPE, newEvent.eventType,
                    FirestoreConstants.EVENT_TIME_AS_STRING, newEvent.getTimeAsString(),
                    FirestoreConstants.EVENT_TIMESTAMP, newEvent.timestamp
                ).await()
        emit(State.success(""))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

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
                }
                .addOnFailureListener { activity.onCreateFailure() }
        } else {
            activity.onInternalFailure("Failure: Event could not be found")
        }
    }

    fun addGiftIdeaToEvent(event: Event, giftIdea: GiftIdea) = flow<State<String>>{
        emit(State.loading())

    }

    fun removeEventGiftIdea(activity: CRUDActivity.GiftIdeaCRUDActivity, oldEvent: Event, giftIdea: GiftIdea) {
        val id = getEventId(oldEvent)
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
        val id = getEventId(event)
        if(id != "") {
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(id).get()
                .addOnSuccessListener {
                    initInvitation(it.toObject(Event::class.java)!!, invitedUser, id, activity)
                }
        } else {
            activity.onInternalFailure("Failure: Event could not be found")
        }
    }

    private fun initInvitation(event: Event, invitedUser: PlandoraUser, eventId: String, activity: CRUDActivity.InvitationCRUDActivity) {
        if(!event.invitedUserIds.contains(invitedUser.id)) {
            val invitation = EventInvitation(PlandoraUserController().currentUserId(), invitedUser.id, eventId, System.currentTimeMillis())
            addInvitationToEvent(invitation, eventId, invitedUser, activity)
        } else {
            activity.onInvitationExists()
        }
    }

    private fun addInvitationToEvent(invitation: EventInvitation, eventId: String, invitedUser: PlandoraUser, activity: CRUDActivity.InvitationCRUDActivity) {
        firestoreInstance.collection(FirestoreConstants.INVITATIONS)
                .document()
                .set(invitation, SetOptions.merge())
                .addOnSuccessListener {
                    firestoreInstance.collection(FirestoreConstants.EVENTS).document(eventId)
                            .update("invitedUserIds", FieldValue.arrayUnion(invitedUser.id))
                    activity.onInvitationCreateSuccess(invitedUser)
                }
                .addOnFailureListener {
                    activity.onInvitationCreateFailure()
                }
    }

    private fun getEventId(event: Event): String {
        var id = ""
        for (entry: MutableMap.MutableEntry<String, Event> in events.entries) {
            if(entry.value == event) {
                id = entry.key
            }
        }
        return id
    }

}