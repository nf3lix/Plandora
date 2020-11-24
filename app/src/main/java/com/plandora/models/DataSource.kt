package com.plandora.models

class DataSource {

    companion object {

        // create some sample items
        fun createDataSet(): ArrayList<Event> {
            val list = ArrayList<Event>()
            var timestamp = 1604498694000
            for(i in 0..10) {
                list.add(Event("Event $i", EventType.ANNIVERSARY, "Test", true, timestamp))
                timestamp += 100000
            }
            for(i in 0..10) {
                list.add(Event("Event $i", EventType.BIRTHDAY, "Test", true, timestamp))
                timestamp += 84700000
            }
            for(i in 0..10) {
                list.add(Event("Event $i", EventType.OTHERS, "Test", true, timestamp))
                timestamp += 84700000
            }
            for(i in 0..10) {
                list.add(Event("Event $i", EventType.HOLIDAY, "Test", true, timestamp))
                timestamp += 84700000
            }
            return list
        }

    }

}