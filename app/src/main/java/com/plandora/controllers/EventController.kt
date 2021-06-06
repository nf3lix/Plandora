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

class EventController {

    companion object {
        val eventList: ArrayList<Event> = ArrayList()
        val events: HashMap<String, Event> = HashMap()
    }

    private val firestoreInstance = FirebaseFirestore.getInstance()

    fun createEvent(event: Event) = flow<State<String>> {
        emit(State.loading())
        firestoreInstance.collection(FirestoreConstants.EVENTS).document().set(event, SetOptions.merge()).await()
        eventList.add(event)
        emit(State.success(""))
    }.catch {
        emit(State.failed(it.message.toString()))
    }

    fun updateEventList() = flow<State<String>> {
        emit(State.loading())
        val document = fetchEventListQuerySnapshot()
        transformEventListDocument(document)
        emit(State.success(""))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun fetchEventListQuerySnapshot(): QuerySnapshot {
        return firestoreInstance
                .collection(FirestoreConstants.EVENTS)
                .whereArrayContains(FirestoreConstants.ATTENDEES, UserController().currentUserId())
                .get().await()
    }

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
        val eventId = getEventId(oldEvent)
        if(eventId.isEmpty()) {
            emit(State.failed("Error: Event could not be found"))
            return@flow
        }
        updateEventDocumentFields(eventId, newEvent)
        emit(State.success(""))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun updateEventDocumentFields(eventId: String, event: Event) {
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(eventId).update(
                FirestoreConstants.EVENT_TITLE, event.title,
                FirestoreConstants.EVENT_DESCRIPTION, event.description,
                FirestoreConstants.EVENT_ANNUAL, event.annual,
                FirestoreConstants.EVENT_DATE_AS_STRING, event.getDateAsString(),
                FirestoreConstants.EVENT_TYPE, event.eventType,
                FirestoreConstants.EVENT_TIME_AS_STRING, event.getTimeAsString(),
                FirestoreConstants.EVENT_TIMESTAMP, event.timestamp
        ).await()
    }

    fun acceptInvitation(eventId: String) = flow<State<String>>{
        emit(State.loading())
        addCurrentUserIdToEvent(eventId)
        emit(State.success(eventId))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun addCurrentUserIdToEvent(eventId: String) {
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(eventId)
                .update(FirestoreConstants.ATTENDEES, FieldValue.arrayUnion(UserController().currentUserId())).await()
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(eventId)
            .update(FirestoreConstants.EVENT_INVITED_USER_IDS, FieldValue.arrayRemove(UserController().currentUserId())).await()
    }

    fun addGiftIdeaToEvent(event: Event, giftIdea: GiftIdea) = flow<State<String>>{
        emit(State.loading())
        val eventId = getEventId(event)
        if(eventId.isEmpty()) {
            emit(State.failed("Event could not be found"))
            return@flow
        }
        if(events[eventId]!!.giftIdeas.contains(giftIdea)) {
            emit(State.failed("This idea already exists"))
            return@flow
        }
        addGiftIdeaToEventDocument(eventId, giftIdea)
        emit(State.success(""))
        events[eventId]?.giftIdeas?.add(giftIdea)
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun addGiftIdeaToEventDocument(eventId: String, giftIdea: GiftIdea) {
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(eventId)
                .update(FirestoreConstants.GIFT_IDEAS, FieldValue.arrayUnion(giftIdea)).await()
    }

    fun removeGiftIdeaFromEvent(event: Event, giftIdea: GiftIdea) = flow<State<String>> {
        emit(State.loading())
        val eventId = getEventId(event)
        if(eventId.isEmpty()) {
            emit(State.failed("Failure: Event could not be found"))
            return@flow
        }
        removeGiftIdeaFromEventDocument(eventId, giftIdea)
        emit(State.success(""))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun removeGiftIdeaFromEventDocument(eventId: String, giftIdea: GiftIdea) {
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(eventId)
                .update(FirestoreConstants.GIFT_IDEAS, FieldValue.arrayRemove(giftIdea)).await()
        events[eventId]?.giftIdeas?.remove(giftIdea)
    }

    fun sendEventInvitation(event: Event, invitedUser: PlandoraUser) = flow<State<String>> {
        emit(State.loading())
        val eventId = getEventId(event)
        if(eventId.isEmpty()) {
            emit(State.failed("Failure: Event could not be found"))
            return@flow
        }
        val creationSuccessful = createInvitation(eventId, getEventFromId(eventId), invitedUser)
        if(creationSuccessful) {
            emit(State.success(""))
        } else {
            emit(State.failed(""))
        }
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun createInvitation(eventId: String, event: Event, invitedUser: PlandoraUser): Boolean {
        if(!event.invitedUserIds.contains(invitedUser.id)) {
            val invitation = EventInvitation(invitedUser.id, UserController().currentUserId(), eventId, System.currentTimeMillis())
            addInvitationToFirestoreCollections(eventId, invitation, invitedUser)
            return true
        }
        return false
    }

    private suspend fun addInvitationToFirestoreCollections(eventId: String, invitation: EventInvitation, invitedUser: PlandoraUser) {
        firestoreInstance.collection(FirestoreConstants.INVITATIONS).document().set(invitation, SetOptions.merge()).await()
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(eventId).update(FirestoreConstants.EVENT_INVITED_USER_IDS, FieldValue.arrayUnion(invitedUser.id)).await()
    }

    fun getEventById(eventId: String) = flow<State<Event>> {
        emit(State.loading())
        val event = getEventFromId(eventId)
        emit(State.success(event))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun getEventFromId(eventId: String): Event {
        val document = firestoreInstance.collection(FirestoreConstants.EVENTS).document(eventId).get().await()
        return document.toObject(Event::class.java)!!
    }

    private fun getEventId(event: Event): String {
        var eventId = ""
        for (entry: MutableMap.MutableEntry<String, Event> in events.entries) {
            if(entry.value == event) {
                eventId = entry.key
            }
        }
        return eventId
    }

    fun removeAttendee(userId: String, event: Event) = flow<State<String>> {
        emit(State.loading())
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(getEventId(event))
            .update(FirestoreConstants.ATTENDEES, FieldValue.arrayRemove(userId)).await()
        emit(State.Success(""))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun deleteEvent(event: Event) = flow<State<String>> {
        emit(State.loading())
        val eventId = getEventId(event)
        firestoreInstance.collection(FirestoreConstants.EVENTS).document(eventId).delete()
        emit(State.success("Event deleted"))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}