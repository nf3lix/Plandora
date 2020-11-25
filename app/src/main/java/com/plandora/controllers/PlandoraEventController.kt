package com.plandora.controllers

import com.google.firebase.firestore.FirebaseFirestore
import com.plandora.activity.CreateEventActivity
import com.plandora.models.Event
import com.plandora.utils.constants.FirestoreConstants

class PlandoraEventController {

    private val firestoreInstance = FirebaseFirestore.getInstance()

    fun createEvent(activity: CreateEventActivity, event: Event) {
        firestoreInstance.collection(FirestoreConstants.EVENTS).document()
    }

}