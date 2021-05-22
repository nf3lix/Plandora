package com.plandora.activity.components.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.plandora.R
import com.plandora.controllers.State
import com.plandora.controllers.UserController
import com.plandora.models.gift_ideas.GiftIdea
import com.plandora.models.gift_ideas.GiftIdeaUIWrapper
import kotlinx.android.synthetic.main.dialog_gift_idea_details.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GiftIdeaDialog(private val context: Context, private val view: ViewGroup?, private val giftIdea: GiftIdea) {

    private val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.dialog_gift_idea_details, view, false)
    private val uiScope = CoroutineScope(Dispatchers.Main)

    companion object {
        private const val POSITIVE_BUTTON_TEXT: String = "Ok"
    }

    fun showDialog() {
        viewInflated.gift_idea_title.text = giftIdea.title
        viewInflated.gift_idea_description.text = giftIdea.description
        AlertDialog.Builder(context)
            .setView(viewInflated)
            .setPositiveButton(POSITIVE_BUTTON_TEXT) { _, _ ->
            }
        .show()
        uiScope.launch {
            setCreatorName(GiftIdeaUIWrapper.createFromGiftIdea(giftIdea))
        }
    }

    private suspend fun setCreatorName(giftIdea: GiftIdeaUIWrapper) {
        UserController().getUserById(giftIdea.ownerId).collect { state ->
            when(state) {
                is State.Loading -> {}
                is State.Success -> { viewInflated.gift_idea_description.text = state.data.displayName }
                is State.Failed -> {}
            }
        }
    }

}