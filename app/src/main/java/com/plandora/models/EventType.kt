package com.plandora.models

import com.plandora.R

enum class EventType(val iconId: Int) {

    BIRTHDAY(R.drawable.event_type_birthday) {

    },

    HOLIDAY(R.drawable.event_type_holiday) {

    },

    ANNIVERSARY(R.drawable.event_type_anniversary) {

    },

    OTHERS(R.drawable.event_type_others) {

    };

    override fun toString(): String {
        return this.name
    }

}