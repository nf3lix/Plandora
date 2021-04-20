package com.plandora.controllers

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.plandora.activity.launch.SignUpActivity
import com.plandora.models.PlandoraUser
import com.plandora.utils.constants.FirestoreConstants
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class PlandoraUserController {

    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun currentUserId(): String {
        val currentUser = firebaseAuth.currentUser
        return when(currentUser != null) {
            false -> ""
            else -> currentUser.uid
        }
    }

    fun getUserFromId(userId: String): PlandoraUser {
        // not implemented yet
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

    fun signUpUser(activity: SignUpActivity, uniqueName: String, displayName: String, email: String, password: String) {
        firestoreInstance.collection(FirestoreConstants.USER_NAMES)
            .document(uniqueName).set(mapOf("user" to email))
            .addOnSuccessListener {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
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
            .addOnFailureListener {
                activity.onSignUpFailed("This unique name is not available!")
            }
    }

    private fun createFireStoreUserDocument(activity: SignUpActivity, user: PlandoraUser) {
        firestoreInstance.collection(FirestoreConstants.USERS)
            .document(currentUserId())
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                firebaseAuth.currentUser!!.sendEmailVerification().addOnSuccessListener {
                    Toast.makeText(activity, "Please check your mails and click the confirmation link we sent you!", Toast.LENGTH_LONG).show()
                }
                activity.onSignUpSuccess()
            }
            .addOnFailureListener {
                activity.onSignUpFailed(it.message!!)
            }
    }


}