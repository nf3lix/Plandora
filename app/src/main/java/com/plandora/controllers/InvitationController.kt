package com.plandora.controllers

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.plandora.models.events.Event
import com.plandora.models.events.EventInvitation
import com.plandora.models.events.EventInvitationStatus
import com.plandora.utils.constants.FirestoreConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class InvitationController {

    companion object {
        private val invitations: HashMap<String, EventInvitation> = HashMap()
        private val invitedEvents: HashMap<String, Event> = HashMap()

        fun getAllInvitations(): ArrayList<EventInvitation> {
            return ArrayList(invitations.values)
        }

        fun getEventFromInvitation(eventInvitation: EventInvitation): Event? {
            return invitedEvents[eventInvitation.eventId]
        }

        fun getInvitationById(key: String): EventInvitation? {
            return invitations[key]
        }

    }

    private val firestoreInstance = FirebaseFirestore.getInstance()

    fun updateInvitationList() = flow<State<String>> {
        emit(State.loading())
        invitations.clear()
        addInvitationsFromQuerySnapshot(fetchEventListQuerySnapshot())
        emit(State.success(""))
    }.catch {
        emit(State.failed("Invitations couldn't be loaded."))
    }.flowOn(Dispatchers.IO)

    fun updateInvitationStatus(invitation: EventInvitation, newStatus: EventInvitationStatus) = flow<State<String>> {
        emit(State.loading())
        setInvitationStatus(invitation, newStatus)
        emit(State.success(""))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun setInvitationStatus(invitation: EventInvitation, newStatus: EventInvitationStatus) {
        firestoreInstance.collection(FirestoreConstants.INVITATIONS).document(getInvitationId(invitation))
                .update(FirestoreConstants.INVITATION_STATUS, newStatus).await()
    }

    private suspend fun fetchEventListQuerySnapshot(): QuerySnapshot {
        return firestoreInstance
            .collection(FirestoreConstants.INVITATIONS)
            .whereEqualTo(FirestoreConstants.INVITED_USER_ID, UserController().currentUserId())
            .get().await()
    }

    fun getInvitedEvents() = flow<State<String>> {
        emit(State.loading())
        val document = fetchEvents()
        transformEventListDocument(document)
        emit(State.success(""))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun fetchEvents(): QuerySnapshot {
        return firestoreInstance
            .collection(FirestoreConstants.EVENTS)
            .whereArrayContains(FirestoreConstants.EVENT_INVITED_USER_IDS, UserController().currentUserId())
            .get().await()
    }

    private fun transformEventListDocument(querySnapshot: QuerySnapshot) {
        invitedEvents.clear()
        addEventsFromQuerySnapshot(querySnapshot)
    }

    private fun addEventsFromQuerySnapshot(querySnapshot: QuerySnapshot) {
        querySnapshot.forEach { document ->
            invitedEvents[document.id] = document.toObject(Event::class.java)
        }
    }

    private fun addInvitationsFromQuerySnapshot(querySnapshot: QuerySnapshot) {
        querySnapshot.forEach { document ->
            val invitation = document.toObject(EventInvitation::class.java)
            if(invitation.status == EventInvitationStatus.PENDING)
                invitations[document.id] = invitation
        }
    }

    private fun getInvitationId(invitation: EventInvitation): String {
        var invitationId = ""
        for (entry: MutableMap.MutableEntry<String, EventInvitation> in invitations.entries) {
            if(entry.value == invitation) {
                invitationId = entry.key
            }
        }
        return invitationId
    }

}