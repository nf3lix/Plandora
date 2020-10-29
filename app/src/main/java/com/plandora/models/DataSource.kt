package com.plandora.models

class DataSource {

    companion object {


        fun createDataSet(): ArrayList<Event> {
            val list = ArrayList<Event>()

            for(i in 0..30) {
                list.add(Event("Event $i", EventType.ANNIVERSARY, "Test"))
            }

            return list

        }

    }

}