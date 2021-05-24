package com.plandora.models

import com.plandora.models.events.EventInvitation
import com.plandora.models.events.EventInvitationStatus
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EventInvitationUnitTest {

    lateinit var invitation: EventInvitation

    companion object {
        private const val SAMPLE_TIMESTAMP_2019 = 1546300800000
        private const val SAMPLE_TIMESTAMP_2020 = 1577836800000
        private const val SAMPLE_TIMESTAMP_2021 = 1609459200000
    }

    @Before
    fun initInvitation() {
        invitation = createInvitation(SAMPLE_TIMESTAMP_2020)
    }

    @Test
    fun compareInvitations_isCorrect() {
        val newInvitation = createInvitation(SAMPLE_TIMESTAMP_2021)
        val oldInvitation = createInvitation(SAMPLE_TIMESTAMP_2019)
        assertTrue(invitation < newInvitation)
        assertTrue(invitation > oldInvitation)
    }

    private fun createInvitation(timestamp: Long): EventInvitation {
        return EventInvitation(
                "INVITED_USER_ID", "INVITATION_OWNER_ID",
                "EVENT_ID", timestamp, EventInvitationStatus.PENDING)
    }

}