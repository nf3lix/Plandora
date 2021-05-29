package com.plandora.activity.launch

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.plandora.R
//import com.plandora.models.validation_types.SignUpValidationTypes
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

class SignUpActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<SignUpActivity>
            = ActivityTestRule(SignUpActivity::class.java)

   /* @Test
    fun showToastIfUniqueNameIsEmpty() {
        onView(withId(R.id.btn_sign_up)).perform(click())
        onView(withText(activityRule.activity.baseContext.getString(SignUpValidationTypes.EMPTY_UNIQUE_NAME.messageId)))
            .inRoot(RootMatchers.withDecorView(Matchers.not(Matchers.`is`(activityRule.activity.window.decorView))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun showToastIfDisplayNameIsEmpty() {
        onView(withId(R.id.unique_name_input)).perform(typeText("UNIQUE_NAME"))
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(R.id.btn_sign_up)).perform(click())
        onView(withText(activityRule.activity.baseContext.getString(SignUpValidationTypes.EMPTY_DISPLAY_NAME.messageId)))
            .inRoot(RootMatchers.withDecorView(Matchers.not(Matchers.`is`(activityRule.activity.window.decorView))))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun showToastIfEmailIsEmpty() {
        onView(withId(R.id.unique_name_input)).perform(typeText("UNIQUE_NAME"))
        onView(withId(R.id.display_name_input)).perform(typeText("DISPLAY_NAME"))
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(R.id.btn_sign_up)).perform(click())
        onView(withText(activityRule.activity.baseContext.getString(SignUpValidationTypes.EMPTY_EMAIL.messageId)))
            .inRoot(RootMatchers.withDecorView(Matchers.not(Matchers.`is`(activityRule.activity.window.decorView))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun showToastIfPasswordIsEmpty() {
        onView(withId(R.id.unique_name_input)).perform(typeText("UNIQUE_NAME"))
        onView(withId(R.id.display_name_input)).perform(typeText("DISPLAY_NAME"))
        onView(withId(R.id.email_input)).perform(typeText("EMAIL"))
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(R.id.btn_sign_up)).perform(click())
        onView(withText(activityRule.activity.baseContext.getString(SignUpValidationTypes.EMPTY_PASSWORD.messageId)))
            .inRoot(RootMatchers.withDecorView(Matchers.not(Matchers.`is`(activityRule.activity.window.decorView))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun showToastIfPasswordsDoNotMatch() {
        onView(withId(R.id.unique_name_input)).perform(typeText("UNIQUE_NAME"))
        onView(withId(R.id.display_name_input)).perform(typeText("DISPLAY_NAME"))
        onView(withId(R.id.email_input)).perform(typeText("EMAIL"))
        onView(withId(R.id.password_input)).perform(typeText("PASSWORD_1"))
        onView(withId(R.id.repeat_password_input)).perform(typeText("PASSWORD_2"))
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(R.id.btn_sign_up)).perform(click())
        onView(withText(activityRule.activity.baseContext.getString(SignUpValidationTypes.PASSWORDS_DO_NOT_MATCH.messageId)))
            .inRoot(RootMatchers.withDecorView(Matchers.not(Matchers.`is`(activityRule.activity.window.decorView))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }*/

}