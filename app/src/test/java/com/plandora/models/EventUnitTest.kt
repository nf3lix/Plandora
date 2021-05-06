package com.plandora.models

import com.plandora.models.events.Event
import com.plandora.models.events.EventType
import com.plandora.models.gift_ideas.GiftIdea
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class EventUnitTest {
    lateinit var event: Event

    @Before
    fun initEvent() {
        event = createEvent(3, false)
    }

    @Test
    fun remainingDays_isCorrect() {
        assertEquals(event.remainingDays(), 3)
        assertEquals(createEvent(5, false).remainingDays(), 5)
        assertEquals(createEvent(5, true).remainingDays(), 5)
        val annualEvent = createEvent(-5, true)
        assertNotEquals(annualEvent.remainingDays(), 5)
        assertEquals(annualEvent.remainingDays(), millisToDays(getNextEvent(annualEvent.timestamp)))
    }

    @Test
    fun getDateAsString_isCorrect() {
        val currentTimestamp = System.currentTimeMillis()
        val expectedString = SimpleDateFormat("MM-dd-yyyy").format(Timestamp(currentTimestamp))
        assertEquals(createEvent(currentTimestamp, false).getDateAsString(), expectedString)
    }

    @Test
    fun getTimeAsString_isCorrect() {
        val currentTimestamp = System.currentTimeMillis()
        val expectedString = SimpleDateFormat("HH:mm").format(Timestamp(currentTimestamp))
        assertEquals(createEvent(currentTimestamp, false).getTimeAsString(), expectedString)
    }

    private fun createEvent(remainingDays: Int, annual: Boolean): Event {
        val userList : ArrayList<String> = arrayListOf("ID_1", "ID_2", "ID_3")
        val giftIdeaList : ArrayList<GiftIdea> = arrayListOf(
                GiftIdea("GIFT_IDEA_TITLE", "GIFT_IDEA_DESCRIPTION", "GIFT_IDEA_OWNER_ID", 5F)
        )
        return Event("EVENT_TITLE", EventType.OTHERS, "EVENT_DESCRIPTION", annual,
                (System.currentTimeMillis() + remainingDays * 864e5).roundToLong(), userList, giftIdeaList, "OWNER_ID",
                arrayListOf("INVITATION_1", "INVITATION_2"))
    }

    private fun createEvent(timestamp: Long, annual: Boolean): Event {
        val userList : ArrayList<String> = arrayListOf("ID_1", "ID_2", "ID_3")
        val giftIdeaList : ArrayList<GiftIdea> = arrayListOf(
                GiftIdea("GIFT_IDEA_TITLE", "GIFT_IDEA_DESCRIPTION", "GIFT_IDEA_OWNER_ID", 5F)
        )
        return Event("EVENT_TITLE", EventType.OTHERS, "EVENT_DESCRIPTION", annual,
                timestamp, userList, giftIdeaList, "OWNER_ID",
                arrayListOf("INVITATION_1", "INVITATION_2"))
    }

    private fun millisToDays(millis: Long): Int {
        return (millis / 864e5).roundToInt()
    }

    private fun getNextEvent(timestamp: Long): Long {
        val date = Date(timestamp)
        val calendar = GregorianCalendar()
        calendar.apply {
            time = date
            set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) + 1)
        }
        return calendar.timeInMillis - System.currentTimeMillis()
    }
}