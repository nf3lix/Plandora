package com.plandora.activity.launch

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.plandora.R
import com.plandora.activity.PlandoraActivity
import com.plandora.activity.main.MainActivity
import com.plandora.controllers.PlandoraEventController
import com.plandora.crud_workflows.CRUDActivity
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : PlandoraActivity(), CRUDActivity {

    companion object {
        private const val SIGN_IN_SUCCESS_MESSAGE = "Successfully signed in"
        private const val AUTHENTICATION_ERROR_MESSAGE = "Authentication failed. Please try again"
        private const val EMAIL_NOT_CONFIRMED_MESSAGE = "You must first confirm your email address"
        private const val INVALID_LOGIN_FORM_MESSAGE = "Please enter your login credentials"
    }

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_sign_in)
        setupButtonClickListeners()
    }

    private fun signIn() {
        val email = et_email_sign_in.text.toString()
        val password = et_password_sign_in.text.toString()
        if(!signInFormIsValid(email, password)) {
            showInvalidLoginFormMessage()
            return
        }
        signInWithEmailAndPassword(email, password)
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                resumeAfterSuccessfulSignIn()
            } else {
                showAuthenticationErrorMessage()
            }
        }
    }

    private fun resumeAfterSuccessfulSignIn() {
        if(!userEmailIsVerified()) {
            showEmailConfirmationMessage()
            return
        }
        nextActivityAfterSignIn()
    }

    private fun nextActivityAfterSignIn() {
        Toast.makeText(this, SIGN_IN_SUCCESS_MESSAGE, Toast.LENGTH_LONG).show()
        PlandoraEventController().getEventList(this)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showAuthenticationErrorMessage() {
        Toast.makeText(this, AUTHENTICATION_ERROR_MESSAGE, Toast.LENGTH_LONG).show()
    }

    private fun showEmailConfirmationMessage() {
        Toast.makeText(this, EMAIL_NOT_CONFIRMED_MESSAGE, Toast.LENGTH_LONG).show()
    }

    private fun showInvalidLoginFormMessage() {
        Toast.makeText(this, INVALID_LOGIN_FORM_MESSAGE, Toast.LENGTH_LONG).show()
    }

    private fun userEmailIsVerified(): Boolean {
        return firebaseAuth.currentUser!!.isEmailVerified
    }

    private fun startSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun setupButtonClickListeners() {
        btn_sign_in.setOnClickListener { signIn() }
        btn_sign_up.setOnClickListener { startSignUp() }
    }

    private fun signInFormIsValid(email: String, password: String): Boolean {
        return !(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
    }

    override fun onInternalFailure(message: String) {

    }

}