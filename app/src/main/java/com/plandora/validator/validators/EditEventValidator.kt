package com.plandora.validator.validators

import com.plandora.R
import com.plandora.activity.main.dashboard.EventDetailActivity
import com.plandora.models.events.Event
import com.plandora.validator.ValidationResult
import com.plandora.validator.Validator

class EditEventValidator(private val activity: EventDetailActivity) : Validator {

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
        class EmptyTitleResult(activity: EventDetailActivity) : ValidationResult(Validator.ValidationState.INVALID, activity.getString((R.string.create_event_empty_title)))
        class EventInPastResult(activity: EventDetailActivity) : ValidationResult(Validator.ValidationState.INVALID, activity.getString((R.string.create_event_in_the_past)))
        class ValidEventResult(activity: EventDetailActivity) : ValidationResult(Validator.ValidationState.VALID, activity.getString((R.string.update_event_success)))
    }

}