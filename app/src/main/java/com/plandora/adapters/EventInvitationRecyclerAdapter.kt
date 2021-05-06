package com.plandora.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.plandora.R
import com.plandora.controllers.PlandoraEventController
import com.plandora.models.events.Event
import com.plandora.models.events.EventInvitation
import kotlinx.android.synthetic.main.layout_invitation_list_item.view.*

class EventInvitationRecyclerAdapter (private var invitationList: List<EventInvitation>,
                                      private val onHandleListener: OnHandleInvitationListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InvitationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_invitation_list_item, parent, false), onHandleListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is InvitationViewHolder -> {
                holder.bind(invitationList[position], Event())
            }
        }
    }

    override fun getItemCount(): Int {
        return invitationList.size
    }

    class InvitationViewHolder(itemView: View, private val onHandleListener: OnHandleInvitationListener) : RecyclerView.ViewHolder(itemView), OnHandleInvitationListener {

        private val eventTitle: TextView = itemView.invitation_event_title
        private val ownerName: TextView = itemView.invitation_event_owner
        private val remainingDays: TextView = itemView.invitation_event_remaining_days

        fun bind(eventInvitation: EventInvitation, event: Event) {
            eventTitle.text = PlandoraEventController.events[eventInvitation.eventId]?.title
            ownerName.text = eventInvitation.invitationOwnerId
            remainingDays.text = PlandoraEventController.events[eventInvitation.eventId]?.remainingDays().toString()
        }

        override fun onAcceptListener(position: Int) {
            onHandleListener.onAcceptListener(position)
        }

        override fun onDeclineListener(position: Int) {
            onHandleListener.onDeclineListener(position)
        }

    }

    interface OnHandleInvitationListener {
        fun onAcceptListener(position: Int)
        fun onDeclineListener(position: Int)
    }


}