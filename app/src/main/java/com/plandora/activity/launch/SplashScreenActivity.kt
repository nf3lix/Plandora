package com.plandora.activity.launch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.plandora.R
import com.plandora.activity.main.MainActivity
import com.plandora.controllers.EventController
import com.plandora.controllers.UserController
import com.plandora.controllers.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_SCREEN_DELAY: Long = 2000
    }

    private val uiScope = CoroutineScope(Dispatchers.Main)

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
    }

    private fun startActivityAfterSplashScreen() {
        if(UserController().currentUserId().isNotEmpty()) {
            setUpMainActivity()
            return
        }
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    private fun setUpMainActivity() {
        uiScope.launch {
            loadEvents()
        }
    }

    private fun resumeWithMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private suspend fun loadEvents() {
        EventController().updateEventList().collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> {
                    Toast.makeText(this, "Successfully loaded events", Toast.LENGTH_LONG).show()
                    resumeWithMainActivity()
                }
                is State.Failed -> {
                    Toast.makeText(this, "Could not load events", Toast.LENGTH_LONG).show()
                    resumeWithMainActivity()
                }
            }
        }
    }


}