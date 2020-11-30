package com.plandora.account

import com.google.firebase.firestore.FirebaseFirestore
import com.plandora.activity.CreateEventActivity
import com.plandora.models.Event
import com.plandora.models.PlandoraUser
import com.plandora.utils.FirestoreConstants

class Firestore {

    fun createEvent(activity: CreateEventActivity, event: Event) {
        // TODO: create event in Firebase
    }

    fun getUserFromId(userId: String): PlandoraUser {
        // TODO: return user
        return PlandoraUser(userId, "Felix", "Felix", "test@test.de")
    }

    fun getUserFromName(username: String): PlandoraUser? {
        FirebaseFirestore.getInstance().collection(FirestoreConstants.USERS)
            .whereEqualTo(FirestoreConstants.USER_NAME_FIELD, username).get()
            .addOnSuccessListener { document ->
                document.documents[0].toObject(PlandoraUser::class.java)
                return@addOnSuccessListener
            }
        return null
    }

}