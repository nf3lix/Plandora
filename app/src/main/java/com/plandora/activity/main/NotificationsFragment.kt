package com.plandora.activity.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.plandora.R
import com.plandora.activity.main.dashboard.EventItemSpacingDecoration
import com.plandora.adapters.EventInvitationRecyclerAdapter
import com.plandora.controllers.InvitationController
import com.plandora.controllers.PlandoraEventController
import com.plandora.controllers.PlandoraUserController
import com.plandora.controllers.State
import com.plandora.models.events.Event
import com.plandora.models.events.EventInvitation
import com.plandora.models.events.EventInvitationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment(), EventInvitationRecyclerAdapter.OnHandleInvitationListener {

    private lateinit var rootView: View
    private lateinit var eventInvitationAdapter: EventInvitationRecyclerAdapter
    private var eventInvitationList: ArrayList<EventInvitation> = ArrayList()
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private var currentPosition = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        displayInvitationsFragment(inflater, container)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        uiScope.launch {
            loadInvitations()
        }
    }

    private fun displayInvitationsFragment(inflater: LayoutInflater, container: ViewGroup?) {
        rootView = inflater.inflate(R.layout.fragment_notifications_main, container, false)
        eventInvitationAdapter = EventInvitationRecyclerAdapter(eventInvitationList, this@NotificationsFragment)
        rootView.findViewById<RecyclerView>(R.id.invitations_recycler_view).apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = eventInvitationAdapter
            addItemDecoration(EventItemSpacingDecoration(40))
        }
    }

    private fun addEventInvitationRecyclerView() {
        eventInvitationList.addAll(InvitationController.getAllInvitations())
    }

    private suspend fun loadInvitations() {
        InvitationController().updateInvitationList().collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> {
                    addEventInvitationRecyclerView()
                    eventInvitationAdapter.notifyDataSetChanged()
                }
                is State.Failed -> {
                    Toast.makeText(activity, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private suspend fun loadEvent(eventId: String) {
        PlandoraEventController().getEventById(eventId).collect { state ->
            when(state) {
                is State.Loading -> {}
                is State.Success -> { acceptInvitation(eventId) }
                is State.Failed -> {}
            }
        }
    }

    private suspend fun acceptInvitation(eventId: String) {
        PlandoraEventController().acceptInvitation(eventId).collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> {
                    updateInvitationStatus(eventInvitationList[currentPosition], EventInvitationStatus.ACCEPTED)
                }
                is State.Failed -> {
                    Toast.makeText(activity, "Could not accept invitation", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private suspend fun updateInvitationStatus(invitation: EventInvitation, status: EventInvitationStatus) {
        InvitationController().updateInvitationStatus(invitation, status).collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> {
                    eventInvitationList.removeAt(currentPosition)
                    eventInvitationAdapter.notifyDataSetChanged()
                    Toast.makeText(activity, "Invitation accepted", Toast.LENGTH_LONG).show()
                }
                is State.Failed -> { }
            }
        }
    }

    override fun onAcceptListener(position: Int) {
        currentPosition = position
        uiScope.launch {
            loadEvent(eventInvitationList[position].eventId)
        }
    }

    override fun onDeclineListener(position: Int) {
    }

}