package com.plandora.validator.validators

import com.plandora.R
import com.plandora.activity.CreateEventActivity
import com.plandora.models.events.Event
import com.plandora.validator.ValidationResult
import com.plandora.validator.Validator

class CreateEventValidator(private val activity: CreateEventActivity) : Validator {

    override fun <T> getValidationState(data: T): ValidationResult {
        if(data !is Event) {
            throw Exception()
        }
        return when {
            !data.relevantForDashboard() -> ValidationResults.EventInPastResult(activity)
            data.title.isEmpty() -> ValidationResults.EmptyTitleResult(activity)
            else -> ValidationResults.ValidEventResult(activity)
        }
    }

    sealed class ValidationResults {
        class EmptyTitleResult(activity: CreateEventActivity) : ValidationResult(Validator.ValidationState.INVALID, activity.getString((R.string.create_event_empty_title)))
        class EventInPastResult(activity: CreateEventActivity) : ValidationResult(Validator.ValidationState.INVALID, activity.getString((R.string.create_event_in_the_past)))
        class ValidEventResult(activity: CreateEventActivity) : ValidationResult(Validator.ValidationState.VALID, activity.getString((R.string.create_event_success)))
    }

}