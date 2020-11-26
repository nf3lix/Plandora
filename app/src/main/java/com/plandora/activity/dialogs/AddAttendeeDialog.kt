package com.plandora.activity.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.ViewGroup
import com.plandora.R
import com.plandora.activity.CreateEventActivity
import com.plandora.activity.PlandoraDialog
import com.plandora.controllers.PlandoraUserController
import kotlinx.android.synthetic.main.dialog_add_attendee.view.*

class AddAttendeeDialog(context: Context, view: ViewGroup?, attachToRoot: Boolean, private val activity: CreateEventActivity)
    : PlandoraDialog(context, view, attachToRoot, resource = R.layout.dialog_add_attendee, title = "Add Attendee") {

    override fun onPositiveButtonClick(dialog: DialogInterface, which: Int) {
        val newAttendee = PlandoraUserController().getUserFromName(viewInflated.add_attendee_input.text.toString())
        if(newAttendee == null) {
            viewInflated.add_attendee_input.hint = "User not found"
        } else {
            activity.addAttendee(newAttendee)
            activity.addAttendeesRecyclerView()
            dialog.dismiss()
        }
    }

    override fun onNegativeButtonClick(dialog: DialogInterface, which: Int) {
        dialog.cancel()
    }

}