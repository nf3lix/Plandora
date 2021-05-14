package com.plandora.validator.validators

import com.plandora.models.events.Event
import com.plandora.validator.ValidationResult
import com.plandora.validator.Validator

class EditEventValidator : Validator<Event, EditEventValidator.EditEventValidationResult> {

    override fun getValidationState(data: Event): EditEventValidationResult {
        return when {
            !data.relevantForDashboard() -> ValidationResults.EventInPastResult()
            data.title.isEmpty() -> ValidationResults.EmptyTitleResult()
            else -> ValidationResults.ValidEventResult()
        }
    }

    sealed class ValidationResults {
        class EmptyTitleResult : EditEventValidationResult(Validator.ValidationState.INVALID, "Please specify a title")
        class EventInPastResult : EditEventValidationResult(Validator.ValidationState.INVALID, "This one-time event is in the past")
        class ValidEventResult : EditEventValidationResult(Validator.ValidationState.VALID, "Successfully created event")
    }

    abstract class EditEventValidationResult(val state: Validator.ValidationState, val message: String) : ValidationResult(state, message)

}