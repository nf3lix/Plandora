package com.plandora.activity.launch


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.plandora.R
import com.plandora.activity.main.dashboard.SearchForEventsActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchForEventsActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SearchForEventsActivity::class.java)

    @Test
    fun searchEvent() {
        val searchTestField = onView(
            allOf(
                withId(R.id.search_for_events_title_input),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        searchTestField.check(matches(isDisplayed()))

        val searchButton = onView(
            allOf(
                withId(R.id.btn_search_for_events), withText("Search"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchButton.perform(click())

        val searchSpinner = onView(
            allOf(
                withId(R.id.search_type_spinner),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        searchSpinner.check(matches(isDisplayed()))

        val closeSearch = onView(
            allOf(
                withId(R.id.close_search), withContentDescription("Close search"),
                withParent(withParent(withId(R.id.action_bar))),
                isDisplayed()
            )
        )
        closeSearch.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
