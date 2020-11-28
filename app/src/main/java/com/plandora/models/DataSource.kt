package com.plandora.models

import com.plandora.models.events.Event
import com.plandora.models.events.EventType

class DataSource {

    companion object {

        // create some sample items
        fun createDataSet(): ArrayList<Event> {
            val list = ArrayList<Event>()
            var timestamp = 1604498694000

            for(i in 0..10) {
                list.add(Event("Event $i", EventType.ANNIVERSARY, "Event Description $i", true, timestamp))
                timestamp += 84700000
            }
            for(i in 0..10) {
                list.add(Event("Event $i", EventType.HOLIDAY, "Event Description $i", true, timestamp))
                timestamp += 84700000
            }
            for(i in 0..10) {
                list.add(Event("Event $i", EventType.BIRTHDAY, "Event Description $i", true, timestamp))
                timestamp += 84700000
            }
            for(i in 0..10) {
                list.add(Event("Event $i", EventType.OTHERS, "Event Description $i", true, timestamp))
                timestamp += 84700000
            }
            return list
        }

    }

}