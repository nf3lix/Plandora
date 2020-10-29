package com.plandora.models

import java.time.LocalDateTime

class DataSource {

    companion object {


        fun createDataSet(): ArrayList<Event> {
            val list = ArrayList<Event>()

            list.add(
                Event("Title", EventType.ANNIVERSARY, "Test")
            )

            list.add(
                Event("Title", EventType.ANNIVERSARY, "Test")
            )

            list.add(
                Event("Title", EventType.ANNIVERSARY, "Test")
            )

            list.add(
                Event("Title", EventType.ANNIVERSARY, "Test")
            )

            return list

        }

    }

}