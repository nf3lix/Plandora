package com.plandora.controllers

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.plandora.activity.dialogs.AddAttendeeDialog
import com.plandora.activity.launch.SignUpActivity
import com.plandora.models.PlandoraUser
import com.plandora.models.SignUpForm
import com.plandora.utils.constants.FirestoreConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

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

    fun getUserById(userId: String) = flow<State<PlandoraUser>> {
        emit(State.loading())
        val user = fetchUser(userId)
        emit(State.success(user))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun fetchUser(userId: String): PlandoraUser {
        val document = firestoreInstance.collection(FirestoreConstants.USERS).document(userId).get().await()
        return document.toObject(PlandoraUser::class.java)!!
    }

    fun inviteUserToEvent(username: String, dialog: AddAttendeeDialog) {
        FirebaseFirestore.getInstance().collection(FirestoreConstants.USERS)
            .whereEqualTo(FirestoreConstants.USER_NAME_FIELD, username).get()
            .addOnSuccessListener { document ->
                if(document.documents.size > 0) {
                    val attendee = document.documents[0].toObject(PlandoraUser::class.java)!!
                    dialog.onUserFetched(attendee)
                }
            }
    }

    fun signUpUser(activity: SignUpActivity, model: SignUpForm) {
        firestoreInstance.collection(FirestoreConstants.USER_NAMES)
            .document(model.uniqueName).set(mapOf("user" to model.email))
            .addOnSuccessListener {
                firebaseAuth.createUserWithEmailAndPassword(model.email, model.password)
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            val firebaseUser: FirebaseUser = it.result!!.user!!
                            val userEmail: String = firebaseUser.email!!
                            val user = PlandoraUser(firebaseUser.uid, model.uniqueName, model.displayName, userEmail)
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