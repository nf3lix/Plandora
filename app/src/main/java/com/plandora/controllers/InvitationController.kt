package com.plandora.controllers

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.plandora.models.events.EventInvitation
import com.plandora.utils.constants.FirestoreConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class InvitationController {

    companion object {
        private val invitations: HashMap<String, EventInvitation> = HashMap()

        fun getAllInvitations(): ArrayList<EventInvitation> {
            return ArrayList(invitations.values)
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

    private suspend fun fetchEventListQuerySnapshot(): QuerySnapshot {
        return firestoreInstance
            .collection(FirestoreConstants.INVITATIONS)
            .whereEqualTo(FirestoreConstants.INVITED_USER_ID, PlandoraUserController().currentUserId())
            .get().await()
    }

    private fun addInvitationsFromQuerySnapshot(querySnapshot: QuerySnapshot) {
        querySnapshot.forEach { document ->
            val invitation = document.toObject(EventInvitation::class.java)
            invitations[document.id] = invitation
        }
    }

}