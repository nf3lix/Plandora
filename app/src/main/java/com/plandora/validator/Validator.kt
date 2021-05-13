package com.plandora.validator

interface Validator {

    enum class ValidationState {
        VALID, INVALID, UNKNOWN
    }

    fun <T> getValidationState(data: T): ValidationResult

}