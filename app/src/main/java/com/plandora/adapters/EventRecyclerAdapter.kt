package com.plandora.adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors.getColor
import com.plandora.R
import com.plandora.models.events.Event
import kotlinx.android.synthetic.main.layout_event_list_item.view.*
import kotlin.math.absoluteValue

class EventRecyclerAdapter(
    private var items: List<Event>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return EventViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_event_list_item, parent, false), onClickListener, context
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EventViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    class EventViewHolder(itemView: View, private val onClickListener: OnClickListener, val context: Context) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val eventTitle: TextView = itemView.event_title
        private val eventDescription: TextView = itemView.event_description
        private val eventRemainingDays: TextView = itemView.event_remaining_days
        private val eventIconView: ImageView = itemView.event_icon_image_view

        // Add content and onClickListener to card views and
        fun bind(event: Event) {
            itemView.setOnClickListener(this)
            eventTitle.text = event.title
            eventDescription.text = event.description
            eventIconView.setImageResource(event.eventType.iconId)
            eventRemainingDays.text = itemView.context.getString(R.string.remaining_days_template)
                .replace("{time}", event.remainingDays().toString(), true)

            //Every Event that takes place in less than 10 days should be displayed in colorAccent instead of colorPrimary
            if (event.remainingDays() < 10) eventRemainingDays.setTextColor(context.resources.getColor(R.color.colorAccent))
        }

        override fun onClick(v: View?) {
            onClickListener.onClickListener(adapterPosition)
        }

    }

    interface OnClickListener {
        fun onClickListener(index: Int)
    }

}