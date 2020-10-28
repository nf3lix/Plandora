package com.plandora.activity.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.plandora.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        btn_sign_up.setOnClickListener { signUpNewUser() }
    }

    private fun signUpNewUser() {
        // TODO: validate user info
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

}