package com.plandora.validator.validators

import com.plandora.models.SignUpForm
import com.plandora.validator.ValidationResult
import com.plandora.validator.Validator

class SignUpValidator : Validator<SignUpForm, SignUpValidator.SignUpValidationResult> {

    override fun getValidationState(data: SignUpForm): SignUpValidationResult {
        return when {
            data.uniqueName.isEmpty() -> ValidationResults.EmptyUniqueNameResult()
            data.displayName.isEmpty() -> ValidationResults.EmptyDisplayNameResult()
            data.email.isEmpty() -> ValidationResults.EmptyEmailResult()
            data.password.isEmpty() -> ValidationResults.EmptyPasswordResult()
            (data.password != data.repeatPassword) -> ValidationResults.PasswordDoNotMatchResult()
            else -> ValidationResults.ValidDataResult()
        }
    }

    sealed class ValidationResults {
        class EmptyUniqueNameResult : SignUpValidationResult(Validator.ValidationState.INVALID, "Unique name must not be empty")
        class EmptyDisplayNameResult : SignUpValidationResult(Validator.ValidationState.INVALID, "Display name must not be empty")
        class EmptyEmailResult : SignUpValidationResult(Validator.ValidationState.INVALID, "Email must not be empty")
        class EmptyPasswordResult : SignUpValidationResult(Validator.ValidationState.INVALID, "Password must not be empty")
        class PasswordDoNotMatchResult : SignUpValidationResult(Validator.ValidationState.INVALID, "Passwords do not match")
        class ValidDataResult : SignUpValidationResult(Validator.ValidationState.VALID, "Valid input")
    }

    abstract class SignUpValidationResult(val state: Validator.ValidationState, val message: String) : ValidationResult(state, message)

}