package com.plandora.activity.launch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.plandora.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        
        // hide all screen decoration (status bar etc.)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // start SignInActivity after a short delay
        Handler().postDelayed({
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }, 2000)
    }

}