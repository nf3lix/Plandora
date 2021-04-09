package com.plandora.activity.launch

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.plandora.R
import com.plandora.activity.PlandoraActivity
import com.plandora.activity.main.MainActivity
import com.plandora.controllers.PlandoraEventController
import com.plandora.crud_workflows.CRUDActivity
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : PlandoraActivity(), CRUDActivity {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_sign_in)
        btn_sign_in.setOnClickListener { signIn() }
        btn_sign_up.setOnClickListener { startSignUp() }
    }

    private fun signIn() {
        val email = et_email_sign_in.text.toString()
        val password = et_password_sign_in.text.toString()
        if(!signInFormIsValid(email, password)) {
            Toast.makeText(this, "Please enter your login credentials", Toast.LENGTH_LONG).show()
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    if(firebaseAuth.currentUser!!.isEmailVerified) {
                        Toast.makeText(this, "Successfully signed in", Toast.LENGTH_LONG).show()
                        PlandoraEventController().getEventList(this)
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "You must first confirm your email address", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Authentication failed. Please try again", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun startSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun signInFormIsValid(email: String, password: String): Boolean {
        return !(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
    }

    override fun onInternalFailure(message: String) {

    }

}