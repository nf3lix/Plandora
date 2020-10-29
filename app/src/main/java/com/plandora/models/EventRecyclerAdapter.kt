package com.plandora.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.plandora.R
import kotlinx.android.synthetic.main.layout_event_list_item.view.*

class EventRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Event> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EventViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_event_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is EventViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    fun submitList(blogList: List<Event>) {
        items = blogList
    }

    class EventViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val eventTitle: TextView = itemView.event_title
        private val eventDescription: TextView = itemView.event_description

        fun bind(event: Event) {
            eventTitle.text = event.title
            eventDescription.text = event.description
        }
    }

    interface OnClickListener {
        fun onClickListener(index: Int)
    }

}