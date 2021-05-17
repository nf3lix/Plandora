package com.plandora.validator.validators

import com.plandora.models.SignInForm
import com.plandora.validator.ValidationResult
import com.plandora.validator.Validator

class SignInValidator : Validator<SignInForm, SignInValidator.SignInValidationResult> {

    override fun getValidationState(data: SignInForm): SignInValidationResult {
        return when {
            data.email.isEmpty() -> ValidationResults.EmptyEmailResult()
            data.password.isEmpty() -> ValidationResults.EmptyPasswordResult()
            else -> ValidationResults.ValidDataResult()
        }
    }

    sealed class ValidationResults {
        class EmptyEmailResult : SignInValidationResult(Validator.ValidationState.INVALID, "Email must not be empty")
        class EmptyPasswordResult : SignInValidationResult(Validator.ValidationState.INVALID, "Password must not be empty")
        class ValidDataResult : SignInValidationResult(Validator.ValidationState.VALID, "Valid input")
    }

    abstract class SignInValidationResult(val state: Validator.ValidationState, val message: String) : ValidationResult(state, message)

}