package com.plandora.activity.main.dashboard

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import junit.framework.TestCase

class EventItemSpacingDecorationTest : TestCase() {

    lateinit var eventItemSpacingDecorationTest: EventItemSpacingDecoration

    companion object {
        private const val SAMPLE_PADDING = 100
    }

    public override fun setUp() {
        eventItemSpacingDecorationTest = EventItemSpacingDecoration(SAMPLE_PADDING)
    }

    fun testGetItemOffsets() {
        EventItemSpacingDecoration(100).equals(eventItemSpacingDecorationTest)
    }
}