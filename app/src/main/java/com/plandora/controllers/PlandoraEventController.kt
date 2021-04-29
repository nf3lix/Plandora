package com.plandora.controllers

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
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

    fun addGiftIdeaToEvent(event: Event, giftIdea: GiftIdea) = flow<State<String>>{
        emit(State.loading())
        var id = ""
        for (entry: MutableMap.MutableEntry<String, Event> in events.entries) {
            if(entry.value == event) {
                id = entry.key
                if(entry.value.giftIdeas.contains(giftIdea)) {
                    emit(State.failed("This idea already exists"))
                    return@flow
                }
            }
        }
        if(id.isEmpty()) {
            emit(State.failed("Event could not be found"))
            return@flow
        }
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(id)
                .update(FirestoreConstants.GIFT_IDEAS, FieldValue.arrayUnion(giftIdea)).await()
        emit(State.success(""))
        events[id]?.giftIdeas?.add(giftIdea)
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun removeGiftIdeaFromEvent(event: Event, giftIdea: GiftIdea) = flow<State<String>> {
        emit(State.loading())
        val id = getEventId(event)
        if(id.isEmpty()) {
            emit(State.failed("Failure: Event could not be found"))
            return@flow
        }
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(id)
                .update(FirestoreConstants.GIFT_IDEAS, FieldValue.arrayRemove(giftIdea)).await()
        events[id]?.giftIdeas?.remove(giftIdea)
        emit(State.success(""))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun sendEventInvitation(event: Event, invitedUser: PlandoraUser) = flow<State<String>> {
        emit(State.loading())
        val id = getEventId(event)
        if(id.isEmpty()) {
            emit(State.failed("Failure: Event could not be found"))
            return@flow
        }
        val document = firestoreInstance.collection(FirestoreConstants.EVENTS)
                                        .document(id).get().await()
        val fetchedEvent = document.toObject(Event::class.java)!!
        if(!fetchedEvent.invitedUserIds.contains(invitedUser.id)) {
            val invitation = EventInvitation(PlandoraUserController().currentUserId(), invitedUser.id, id, System.currentTimeMillis())
            firestoreInstance.collection(FirestoreConstants.INVITATIONS)
                    .document()
                    .set(invitation, SetOptions.merge()).await()
            firestoreInstance.collection(FirestoreConstants.EVENTS).document(id)
                    .update("invitedUserIds", FieldValue.arrayUnion(invitedUser.id)).await()
            emit(State.success(""))
        } else {
            emit(State.failed(""))
        }
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

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