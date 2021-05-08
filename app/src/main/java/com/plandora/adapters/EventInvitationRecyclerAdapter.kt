package com.plandora.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.plandora.R
import com.plandora.controllers.PlandoraEventController
import com.plandora.controllers.PlandoraUserController
import com.plandora.controllers.State
import com.plandora.models.events.Event
import com.plandora.models.events.EventInvitation
import kotlinx.android.synthetic.main.layout_invitation_list_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EventInvitationRecyclerAdapter (private var invitationList: List<EventInvitation>,
                                      private val onHandleListener: OnHandleInvitationListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InvitationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_invitation_list_item, parent, false), onHandleListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is InvitationViewHolder -> {
                holder.bind(invitationList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return invitationList.size
    }

    class InvitationViewHolder(itemView: View, private val onHandleListener: OnHandleInvitationListener) : RecyclerView.ViewHolder(itemView), OnHandleInvitationListener {

        private val uiScope = CoroutineScope(Dispatchers.Main)

        private val eventTitle: TextView = itemView.invitation_event_title
        private val ownerName: TextView = itemView.invitation_event_owner
        private val remainingDays: TextView = itemView.invitation_event_remaining_days
        private val acceptButton: ImageView = itemView.btn_accept_invitation
        private val declineButton: ImageView = itemView.btn_decline_invitation

        fun bind(eventInvitation: EventInvitation) {
            acceptButton.setOnClickListener{ onAcceptListener(adapterPosition) }
            declineButton.setOnClickListener{ onDeclineListener(adapterPosition) }
            eventTitle.text = PlandoraEventController.events[eventInvitation.eventId]?.title
            remainingDays.text = PlandoraEventController.events[eventInvitation.eventId]?.remainingDays().toString()
            uiScope.launch {
                loadUsername(eventInvitation.invitationOwnerId)
            }
        }

        override fun onAcceptListener(position: Int) {
            onHandleListener.onAcceptListener(position)
        }

        override fun onDeclineListener(position: Int) {
            onHandleListener.onDeclineListener(position)
        }

        suspend fun loadUsername(userId: String) {
            PlandoraUserController().getUserById(userId).collect { state ->
                when(state) {
                    is State.Loading -> { }
                    is State.Success -> {
                        ownerName.text = state.data.displayName
                    }
                    is State.Failed -> { }
                }
            }
        }

    }

    interface OnHandleInvitationListener {
        fun onAcceptListener(position: Int)
        fun onDeclineListener(position: Int)
    }


}