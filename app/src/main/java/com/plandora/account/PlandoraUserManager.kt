package com.plandora.account

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class PlandoraUserManager {

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

}