package com.plandora.activity.main

import com.plandora.activity.main.dashboard.EventItemSpacingDecoration
import com.plandora.activity.main.dashboard.EventItemSpacingDecorationTest
import junit.framework.TestCase

class ProfileFragmentTest : TestCase() {

    lateinit var profileFragment: ProfileFragment

    public override fun setUp() {
        profileFragment = ProfileFragment()
    }

    fun testOnCreateView() {
        profileFragment.equals(ProfileFragment())
    }
}