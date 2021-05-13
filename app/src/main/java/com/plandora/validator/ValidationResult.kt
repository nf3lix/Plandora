package com.plandora.validator

open class ValidationResult(val validationState: Validator.ValidationState, val validationMessage: String) {

    fun isValid(): Boolean {
        return validationState == Validator.ValidationState.VALID
    }

    fun isInvalid(): Boolean {
        return validationState == Validator.ValidationState.INVALID
    }

    fun isUnknown(): Boolean {
        return validationState == Validator.ValidationState.UNKNOWN
    }


}