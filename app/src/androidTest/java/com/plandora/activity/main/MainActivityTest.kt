package com.plandora.activity.main

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.plandora.R
import com.plandora.activity.launch.SplashScreenActivity
import org.hamcrest.Matchers
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun bottomNavigation() {
        val bottomNavigationView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.bottom_navigation_view),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.fragment_container),
                        ViewMatchers.withParent(ViewMatchers.withId(R.id.drawer_layout))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        bottomNavigationView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val bottomNavDashboard = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.bottom_nav_dashboard),
                ViewMatchers.withContentDescription("Dashboard"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.bottom_navigation_view))),
                ViewMatchers.isDisplayed()
            )
        )
        bottomNavDashboard.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val bottomNavNotifications = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.bottom_nav_notifications),
                ViewMatchers.withContentDescription("Notifications"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.bottom_navigation_view))),
                ViewMatchers.isDisplayed()
            )
        )
        bottomNavNotifications.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val bottomNavProfile = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.bottom_nav_profile),
                ViewMatchers.withContentDescription("Profile"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.bottom_navigation_view))),
                ViewMatchers.isDisplayed()
            )
        )
        bottomNavProfile.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}