package com.plandora.activity.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.ViewGroup
import com.plandora.R
import com.plandora.controllers.PlandoraUserController
import com.plandora.activity.PlandoraDialog
import com.plandora.activity.main.GiftIdeaDialogActivity
import com.plandora.models.gift_ideas.GiftIdeaUIWrapper
import kotlinx.android.synthetic.main.dialog_add_gift_idea.view.*

class AddGiftIdeaDialog(context: Context, view: ViewGroup?, attachToRoot: Boolean, private val activity: GiftIdeaDialogActivity)
    : PlandoraDialog(context, view, attachToRoot, resource = R.layout.dialog_add_gift_idea, title = "Add Gift Idea") {

    companion object {
        private const val EMPTY_TITLE_ERROR: String = "Title must not be empty"
    }

    override fun onPositiveButtonClick(dialog: DialogInterface, which: Int) {
        handlePositiveButtonClick(dialog)
    }

    private fun handlePositiveButtonClick(dialog: DialogInterface) {
        val title = viewInflated.add_gift_idea_title.text.toString()
        handleInvalidTitle(title)
        addNewGiftIdea()
        dialog.dismiss()
    }

    private fun addNewGiftIdea() {
        val newGiftIdea = GiftIdeaUIWrapper(title, viewInflated.add_gift_idea_description.text.toString(),
                PlandoraUserController().currentUserId(), 0.0F, ArrayList()
        )
        activity.addGiftIdea(newGiftIdea)
        activity.addGiftIdeasRecyclerView()
    }

    private fun handleInvalidTitle(title: String) {
        if(!isValidTitle(title)) {
            viewInflated.add_gift_idea_title.hint = EMPTY_TITLE_ERROR
        }
    }

    private fun isValidTitle(title: String): Boolean {
        return title.isNotEmpty()
    }

    override fun onNegativeButtonClick(dialog: DialogInterface, which: Int) {
        dialog.cancel()
    }
}