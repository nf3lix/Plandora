package com.plandora.validators

import com.plandora.activity.CreateEventActivity
import com.plandora.models.events.Event
import com.plandora.models.events.EventType
import com.plandora.validator.Validator
import com.plandora.validator.validators.CreateEventValidator
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CreateEventValidatorTest {

    lateinit var validator: CreateEventValidator

    @Before
    fun createValidator() {
        validator = CreateEventValidator()
    }

    @Test
    fun testValidation() {
        val event1 = Event("", EventType.ANNIVERSARY, "", true, System.currentTimeMillis() + 1E6.toLong())
        assertTrue(validator.getValidationState(event1) is CreateEventValidator.ValidationResults.EmptyTitleResult)
        val event2 = Event("TITLE", EventType.ANNIVERSARY, "", false, System.currentTimeMillis() - 1E9.toLong())
        assertTrue(validator.getValidationState(event2) is CreateEventValidator.ValidationResults.EventInPastResult)
        val event3 = Event("TITLE", EventType.ANNIVERSARY, "", true, System.currentTimeMillis() - 1E9.toLong())
        assertTrue(validator.getValidationState(event3) is CreateEventValidator.ValidationResults.ValidEventResult)
        val event4 = Event("TITLE", EventType.ANNIVERSARY, "", true, System.currentTimeMillis() + 1E6.toLong())
        assertTrue(validator.getValidationState(event4) is CreateEventValidator.ValidationResults.ValidEventResult)
    }

}