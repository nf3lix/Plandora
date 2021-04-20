package com.plandora.models.validation_types

import com.plandora.R

enum class EditEventValidationTypes(val message: Int) {

    SUCCESS(R.string.update_event_success),
    EMPTY_TITLE(R.string.create_event_empty_title),
    EVENT_IN_THE_PAST(R.string.create_event_in_the_past)

}