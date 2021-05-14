package com.plandora.validators

import com.plandora.models.SignInForm
import com.plandora.models.events.Event
import com.plandora.models.events.EventType
import com.plandora.validator.validators.EditEventValidator
import com.plandora.validator.validators.SignInValidator
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SignInValidatorTest {

    private lateinit var validator: SignInValidator

    @Before
    fun createValidator() {
        validator = SignInValidator()
    }

    @Test
    fun testValidation() {
        val model1 = SignInForm("", "")
        assertTrue(validator.getValidationState(model1) is SignInValidator.ValidationResults.EmptyEmailResult)
        val model2 = SignInForm("EMAIL", "")
        assertTrue(validator.getValidationState(model2) is SignInValidator.ValidationResults.EmptyPasswordResult)
        val model3 = SignInForm("EMAIL", "PASSWORD")
        assertTrue(validator.getValidationState(model3) is SignInValidator.ValidationResults.ValidDataResult)
    }

}