package com.plandora.validators

import com.plandora.models.SignUpForm
import com.plandora.validator.validators.SignUpValidator
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SignUpValidatorTest {

    private lateinit var validator: SignUpValidator

    @Before
    fun createValidator() {
        validator = SignUpValidator()
    }

    @Test
    fun testValidation() {
        val model1 = SignUpForm("", "", "", "", "")
        assertTrue(validator.getValidationState(model1) is SignUpValidator.ValidationResults.EmptyUniqueNameResult)
        val model2 = SignUpForm("UNIQUE_NAME", "", "", "", "")
        assertTrue(validator.getValidationState(model2) is SignUpValidator.ValidationResults.EmptyDisplayNameResult)
        val model3 = SignUpForm("UNIQUE_NAME", "DISPLAY_NAME", "", "", "")
        assertTrue(validator.getValidationState(model3) is SignUpValidator.ValidationResults.EmptyEmailResult)
        val model4 = SignUpForm("UNIQUE_NAME", "DISPLAY_NAME", "EMAIL", "", "")
        assertTrue(validator.getValidationState(model4) is SignUpValidator.ValidationResults.EmptyPasswordResult)
        val model5 = SignUpForm("UNIQUE_NAME", "DISPLAY_NAME", "EMAIL", "PW", "PW_1")
        assertTrue(validator.getValidationState(model5) is SignUpValidator.ValidationResults.PasswordDoNotMatchResult)
        val model6 = SignUpForm("UNIQUE_NAME", "DISPLAY_NAME", "EMAIL", "PW", "PW")
        assertTrue(validator.getValidationState(model6) is SignUpValidator.ValidationResults.ValidDataResult)
    }

}