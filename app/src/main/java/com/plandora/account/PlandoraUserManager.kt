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

}