package com.plandora.activity.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.ViewGroup
import com.plandora.R
import com.plandora.controllers.PlandoraUserController
import com.plandora.activity.CreateEventActivity
import com.plandora.activity.PlandoraDialog
import com.plandora.models.GiftIdea
import kotlinx.android.synthetic.main.dialog_add_gift_idea.view.*

class AddGiftIdeaDialog(context: Context, view: ViewGroup?, attachToRoot: Boolean, private val activity: CreateEventActivity)
    : PlandoraDialog(context, view, attachToRoot, resource = R.layout.dialog_add_gift_idea, title = "Add Gift Idea") {

    override fun onPositiveButtonClick(dialog: DialogInterface, which: Int) {
        val title = viewInflated.add_gift_idea_title.text.toString()
        if(title.isEmpty()) {
            viewInflated.add_gift_idea_title.hint = "Title must not be empty"
        } else {
            val newGiftIdea = GiftIdea(
                title,
                viewInflated.add_gift_idea_description.text.toString(),
                PlandoraUserController().currentUserId(),
                0.0F,
                ArrayList()
            )
            activity.addGiftIdea(newGiftIdea)
            dialog.dismiss()
            activity.addGiftIdeasRecyclerView()
        }
    }

    override fun onNegativeButtonClick(dialog: DialogInterface, which: Int) {
        dialog.cancel()
    }
}