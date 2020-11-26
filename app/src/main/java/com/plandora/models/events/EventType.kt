package com.plandora.models.events

import com.plandora.R

enum class EventType(val iconId: Int) {

    BIRTHDAY(R.drawable.event_type_birthday) {

    },

    HOLIDAY(R.drawable.event_type_birthday) {

    },

    ANNIVERSARY(R.drawable.event_type_birthday) {

    },

    OTHERS(R.drawable.event_type_birthday) {

    };

    override fun toString(): String {
        return this.name
    }

}