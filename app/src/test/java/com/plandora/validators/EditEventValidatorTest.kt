package com.plandora.validators

import com.plandora.activity.CreateEventActivity
import com.plandora.models.events.Event
import com.plandora.models.events.EventType
import com.plandora.validator.validators.CreateEventValidator
import com.plandora.validator.validators.EditEventValidator
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EditEventValidatorTest {

    private lateinit var validator: EditEventValidator

    @Before
    fun createValidator() {
        validator = EditEventValidator()
    }

    @Test
    fun testValidation() {
        val event1 = Event("", EventType.ANNIVERSARY, "", true, System.currentTimeMillis() + 1E6.toLong())
        Assert.assertTrue(validator.getValidationState(event1) is EditEventValidator.ValidationResults.EmptyTitleResult)
        val event2 = Event("TITLE", EventType.ANNIVERSARY, "", false, System.currentTimeMillis() - 1E9.toLong())
        Assert.assertTrue(validator.getValidationState(event2) is EditEventValidator.ValidationResults.EventInPastResult)
        val event3 = Event("TITLE", EventType.ANNIVERSARY, "", true, System.currentTimeMillis() - 1E9.toLong())
        Assert.assertTrue(validator.getValidationState(event3) is EditEventValidator.ValidationResults.ValidEventResult)
        val event4 = Event("TITLE", EventType.ANNIVERSARY, "", true, System.currentTimeMillis() + 1E6.toLong())
        Assert.assertTrue(validator.getValidationState(event4) is EditEventValidator.ValidationResults.ValidEventResult)
    }

}