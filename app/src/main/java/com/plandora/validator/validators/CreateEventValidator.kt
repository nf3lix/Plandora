package com.plandora.validator.validators

import com.plandora.models.events.Event
import com.plandora.validator.ValidationResult
import com.plandora.validator.Validator

class CreateEventValidator : Validator<Event, CreateEventValidator.CreateEventValidationResult> {

    override fun getValidationState(data: Event): CreateEventValidationResult {
        return when {
            !data.relevantForDashboard() -> ValidationResults.EventInPastResult()
            data.title.isEmpty() -> ValidationResults.EmptyTitleResult()
            else -> ValidationResults.ValidEventResult()
        }
    }

    sealed class ValidationResults {
        class EmptyTitleResult : CreateEventValidationResult(Validator.ValidationState.INVALID, "Please specify a title")
        class EventInPastResult : CreateEventValidationResult(Validator.ValidationState.INVALID, "This one-time event is in the past")
        class ValidEventResult : CreateEventValidationResult(Validator.ValidationState.VALID, "Successfully created event")
    }

    abstract class CreateEventValidationResult(val state: Validator.ValidationState, val message: String) : ValidationResult(state, message)

}