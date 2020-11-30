package com.plandora.account

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.plandora.activity.launch.SignUpActivity
import com.plandora.models.PlandoraUser
import com.plandora.utils.FirestoreConstants
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

    fun signUpUser(activity: SignUpActivity, uniqueName: String, displayName: String, email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val firebaseUser: FirebaseUser = it.result!!.user!!
                    val userEmail: String = firebaseUser.email!!
                    val user = PlandoraUser(firebaseUser.uid, uniqueName, displayName, userEmail)
                    createFireStoreUserDocument(activity, user)
                } else {
                    activity.onSignUpFailed(it.exception!!.message!!)
                }
            }
    }

    private fun createFireStoreUserDocument(activity: SignUpActivity, user: PlandoraUser) {
        FirebaseFirestore.getInstance().collection(FirestoreConstants.USERS)
            .document(currentUserId())
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.onSignUpSuccess()
            }
            .addOnFailureListener {
                activity.onSignUpFailed(it.message!!)
            }
    }

}