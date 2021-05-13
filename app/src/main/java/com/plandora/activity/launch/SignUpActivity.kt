package com.plandora.activity.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.plandora.R
import com.plandora.controllers.PlandoraUserController
import com.plandora.models.SignUpForm
import com.plandora.validator.Validator
import com.plandora.validator.validators.SignUpValidator
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
        val signUp = SignUpForm(uniqueName, displayName, email, password, repeatPassword)
        validateForm(signUp)
    }

    private fun validateForm(signUpForm: SignUpForm) {
        val state = SignUpValidator(this).getValidationState(signUpForm)
        if(state.validationState == Validator.ValidationState.INVALID) {
            Toast.makeText(this, state.validationMessage, Toast.LENGTH_SHORT).show()
            return
        }
        PlandoraUserController().signUpUser(this, signUpForm.uniqueName, signUpForm.displayName, signUpForm.email, signUpForm.password)
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