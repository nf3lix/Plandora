package com.plandora.controllers

import com.google.firebase.auth.FirebaseAuth
import com.plandora.models.PlandoraUser
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class PlandoraUserController {

    suspend fun signInUser(email: String, password: String, firebaseAuth: FirebaseAuth): Boolean {
        return withContext(IO) {
            true
        }
    }

    fun currentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return when(currentUser != null) {
            false -> ""
            else -> currentUser.uid
        }
    }

    fun getUserFromId(userId: String): PlandoraUser {
        // TODO: return user
        return PlandoraUser(userId, "Felix", "Felix", "test@test.de")
    }

    fun getUserFromName(username: String): PlandoraUser? {
        return PlandoraUser("userId", "Felix", "Felix", "test@test.de")
    }


}