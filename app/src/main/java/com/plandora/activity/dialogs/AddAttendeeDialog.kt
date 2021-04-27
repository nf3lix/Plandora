package com.plandora.activity.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.ViewGroup
import com.plandora.R
import com.plandora.activity.PlandoraDialog
import com.plandora.controllers.PlandoraEventController
import com.plandora.controllers.PlandoraUserController
import com.plandora.crud_workflows.CRUDActivity
import com.plandora.models.PlandoraUser
import com.plandora.models.events.Event
import kotlinx.android.synthetic.main.dialog_add_attendee.view.*

class AddAttendeeDialog(context: Context, view: ViewGroup?, attachToRoot: Boolean, private val event: Event, private val activity: CRUDActivity.InvitationCRUDActivity)
    : PlandoraDialog(context, view, attachToRoot, resource = R.layout.dialog_add_attendee, title = "Add Attendee") {

    override fun onPositiveButtonClick(dialog: DialogInterface, which: Int) {
        PlandoraUserController().inviteUserToEvent(viewInflated.add_attendee_input.text.toString(), this, activity)
        dialog.dismiss()
    }

    override fun onNegativeButtonClick(dialog: DialogInterface, which: Int) {
        dialog.cancel()
    }

    fun onUserFetched(newAttendee: PlandoraUser) {
        PlandoraEventController().createEventInvitation(event, newAttendee, activity)
    }

}