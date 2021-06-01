package com.plandora.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.plandora.R
import com.plandora.validator.validators.CreateEventValidator
import com.plandora.validator.validators.SignUpValidator
//import com.plandora.models.validation_types.CreateEventValidationTypes
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateEventActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<CreateEventActivity>
            = ActivityTestRule(CreateEventActivity::class.java)

    @Test
    fun showToastIfEventIsInThePast() {
        onView(withId(R.id.save_entry)).perform(click())
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withText(CreateEventValidator.ValidationResults.EmptyTitleResult().message))
            .inRoot(RootMatchers.withDecorView(Matchers.not(`is`(activityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

}