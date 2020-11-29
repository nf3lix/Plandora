package com.plandora.models

import com.plandora.R

enum class CreateEventValidationTypes(val message: Int) {

    SUCCESS(R.string.create_event_success),
    EMPTY_TITLE(R.string.create_event_empty_title),
    EVENT_IN_THE_PAST(R.string.create_event_in_the_past)

}