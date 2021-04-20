package com.plandora.activity.launch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.plandora.R
import com.plandora.activity.main.MainActivity
import com.plandora.controllers.PlandoraEventController
import com.plandora.controllers.PlandoraUserController
import com.plandora.crud_workflows.CRUDActivity

class SplashScreenActivity : AppCompatActivity(), CRUDActivity {

    companion object {
        private const val SPLASH_SCREEN_DELAY: Long = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        hideScreenDecoration()
        resumeAfterDelay()
    }

    private fun hideScreenDecoration() {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun resumeAfterDelay() {
        Handler().postDelayed({
            resumeAfterSplashScreen()
        }, SPLASH_SCREEN_DELAY)
    }

    private fun resumeAfterSplashScreen() {
        startActivityAfterSplashScreen()
        finish()
    }

    private fun startActivityAfterSplashScreen() {
        if(PlandoraUserController().currentUserId().isNotEmpty()) {
            setUpMainActivity()
            return
        }
        startActivity(Intent(this, SignInActivity::class.java))
    }

    private fun setUpMainActivity() {
        PlandoraEventController().getEventList(this)
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onInternalFailure(message: String) {
        Toast.makeText(this, "An internal error occured", Toast.LENGTH_LONG).show()
    }


}