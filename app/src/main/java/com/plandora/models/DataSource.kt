package com.plandora.models

class DataSource {

    companion object {


        fun createDataSet(): ArrayList<Event> {
            val list = ArrayList<Event>()
            var timestamp = 1604498694000

            for(i in 0..30) {
                list.add(Event("Event $i", EventType.ANNIVERSARY, "Test", true, timestamp))
                timestamp += 84700000
            }

            return list

        }

    }

}