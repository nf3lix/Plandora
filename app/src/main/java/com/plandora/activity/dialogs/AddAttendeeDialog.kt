package com.plandora.activity.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.ViewGroup
import android.widget.Toast
import com.plandora.R
import com.plandora.activity.PlandoraDialog
import com.plandora.activity.main.dashboard.EventDetailActivity
import com.plandora.controllers.EventController
import com.plandora.controllers.UserController
import com.plandora.controllers.State
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import kotlinx.android.synthetic.main.dialog_add_attendee.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddAttendeeDialog(context: Context, view: ViewGroup?, attachToRoot: Boolean, private val event: Event, private val activity: EventDetailActivity)
    : PlandoraDialog(context, view, attachToRoot, resource = R.layout.dialog_add_attendee, title = "Add Attendee") {

    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onPositiveButtonClick(dialog: DialogInterface, which: Int) {
        UserController().inviteUserToEvent(viewInflated.add_attendee_input.text.toString(), this)
        dialog.dismiss()
    }

    override fun onNegativeButtonClick(dialog: DialogInterface, which: Int) {
        dialog.cancel()
    }

    fun onUserFetched(newAttendee: PlandoraUser) {
        uiScope.launch {
            sendEventInvitation(event, newAttendee)
        }
    }

    private suspend fun sendEventInvitation(event: Event, invitedUser: PlandoraUser) {
        EventController().sendEventInvitation(event, invitedUser).collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> {
                    Toast.makeText(context, "User successfully invited", Toast.LENGTH_LONG).show()
                    activity.addAttendeeToList(invitedUser)
                    activity.addAttendeesRecyclerView()
                }
                is State.Failed -> { Toast.makeText(context, "Could not invite user", Toast.LENGTH_LONG).show() }
            }
        }
    }

}