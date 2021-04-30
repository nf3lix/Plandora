package com.plandora.activity.launch

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.plandora.R
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<SignInActivity>
            = ActivityTestRule(SignInActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun startSignUpActivityOnClick() {
        onView(withId(R.id.btn_sign_up)).perform(click())
        intended(hasComponent(SignUpActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun showToastOnSignInButtonClicked() {
        onView(withId(R.id.btn_sign_in)).perform(click())
        onView(withText("Please enter your login credentials"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(`is`(activityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

}