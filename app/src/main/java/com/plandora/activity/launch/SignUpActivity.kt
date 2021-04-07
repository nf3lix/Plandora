package com.plandora.activity.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.plandora.R
import com.plandora.controllers.PlandoraUserController
import com.plandora.models.validation_types.SignUpValidationTypes
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        btn_sign_up.setOnClickListener { signUpNewUser() }
    }

    private fun signUpNewUser() {
        val uniqueName = unique_name_input.text.toString()
        val displayName = display_name_input.text.toString()
        val email = email_input.text.toString()
        val password = password_input.text.toString()
        val repeatPassword = repeat_password_input.text.toString()
        val validationId = validateForm(uniqueName, displayName, email, password, repeatPassword)
        if(validationId != SignUpValidationTypes.SUCCESS) {
            Toast.makeText(this, getString(validationId.messageId), Toast.LENGTH_SHORT).show()
        } else {
            PlandoraUserController().signUpUser(this, uniqueName, displayName, email, password)
        }
    }

    private fun validateForm(uniqueName: String, displayName: String, email: String,
                             password: String, repeatPassword: String): SignUpValidationTypes {
        return when {
            TextUtils.isEmpty(uniqueName) -> SignUpValidationTypes.EMPTY_UNIQUE_NAME
            TextUtils.isEmpty(displayName) -> SignUpValidationTypes.EMPTY_DISPLAY_NAME
            TextUtils.isEmpty(email) -> SignUpValidationTypes.EMPTY_EMAIL
            TextUtils.isEmpty(password) -> SignUpValidationTypes.EMPTY_PASSWORD
            (password != repeatPassword) -> SignUpValidationTypes.PASSWORDS_DO_NOT_MATCH
            else -> SignUpValidationTypes.SUCCESS
        }
    }

    fun onSignUpSuccess() {
        Toast.makeText(this, "Successfully signed up", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    fun onSignUpFailed(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}