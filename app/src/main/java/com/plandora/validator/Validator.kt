package com.plandora.validator

interface Validator<T: Any, R: ValidationResult> {

    enum class ValidationState {
        VALID, INVALID, UNKNOWN
    }

    fun getValidationState(data: T): R

}