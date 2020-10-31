package com.plandora.activity.main.dashboard

import android.os.Bundle
import com.plandora.R
import com.plandora.models.Event
import com.plandora.activity.PlandoraActivity
import kotlinx.android.synthetic.main.activity_event_detail.*

class EventDetailActivity : PlandoraActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        val event = intent.getParcelableExtra<Event>("event_object")!!
        event_detail_title.text = event.title
    }

}