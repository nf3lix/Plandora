package com.plandora.account

import com.plandora.activity.CreateEventActivity
import com.plandora.models.Event
import com.plandora.models.PlandoraUser

class Firestore {

    fun createEvent(activity: CreateEventActivity, event: Event) {
        // TODO: create event in Firebase
    }

    fun getUserFromId(userId: String): PlandoraUser {
        // TODO: return user
        return PlandoraUser(userId, "Felix", "Felix", "test@test.de")
    }

    fun getUserFromName(username: String): PlandoraUser? {
        return PlandoraUser("userId", "Felix", "Felix", "test@test.de")
    }

}