package com.plandora.activity.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.plandora.R
import com.plandora.models.SignUpValidationTypes
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        btn_sign_up.setOnClickListener { signUpNewUser() }
    }

    private fun signUpNewUser() {
        val validationId = validateForm(
            unique_name_input.text.toString(),
            display_name_input.text.toString(),
            email_input.text.toString(),
            password_input.text.toString(),
            repeat_password_input.text.toString())
        if(validationId != SignUpValidationTypes.SUCCESS) {
            Toast.makeText(this, getString(validationId.messageId), Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun validateForm(uniqueName: String, displayName: String, email: String,
                             password: String, repeatPassword: String): SignUpValidationTypes {
        return when {
            TextUtils.isEmpty(uniqueName) -> SignUpValidationTypes.EMPTY_UNIQUE_NAME
            TextUtils.isEmpty(displayName) -> SignUpValidationTypes.EMPTY_UNIQUE_NAME
            TextUtils.isEmpty(email) -> SignUpValidationTypes.EMPTY_UNIQUE_NAME
            TextUtils.isEmpty(password) -> SignUpValidationTypes.EMPTY_UNIQUE_NAME
            (password != repeatPassword) -> SignUpValidationTypes.PASSWORDS_DO_NOT_MATCH
            else -> SignUpValidationTypes.SUCCESS
        }
    }

}