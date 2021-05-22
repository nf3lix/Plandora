package com.plandora.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.plandora.R
import com.plandora.controllers.UserController
import com.plandora.controllers.State
import com.plandora.models.gift_ideas.GiftIdeaUIWrapper
import kotlinx.android.synthetic.main.layout_gift_ideas_list_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GiftIdeaRecyclerAdapter(
    private var items: List<GiftIdeaUIWrapper>,
    private var giftIdeaClickListener: GiftIdeaClickListener,
    private val multiSelect: Boolean = true) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectedItemPos = -1
    var lastItemSelectedPos = -1

    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GiftIdeaSingleSelectViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.layout_gift_ideas_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is GiftIdeaSingleSelectViewHolder -> {
                if(position == selectedItemPos) {
                    holder.select(items[position])
                } else {
                    holder.deselect(items[position])
                }
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getSelectedItems(): ArrayList<GiftIdeaUIWrapper> {
        val selectedItems = ArrayList<GiftIdeaUIWrapper>()
        for(giftIdea: GiftIdeaUIWrapper in items) {
            if(giftIdea.selected) selectedItems.add(giftIdea)
        }
        return selectedItems
    }

    inner class GiftIdeaSingleSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.gift_idea_title
        private val creator: TextView = itemView.gift_idea_creator
        private val ratingBar: RatingBar = itemView.gift_idea_rating

        fun bind(giftIdea: GiftIdeaUIWrapper) {
            title.text = giftIdea.title
            ratingBar.rating = giftIdea.rating
            uiScope.launch {
                setCreatorName(giftIdea)
            }

            itemView.gift_idea_card_view.setOnLongClickListener {
                selectedItemPos = adapterPosition
                if(items[selectedItemPos].selected) {
                    deselect(items[selectedItemPos])
                    selectedItemPos = -1
                } else if(lastItemSelectedPos == -1) {
                    lastItemSelectedPos = selectedItemPos
                } else {
                    notifyItemChanged(lastItemSelectedPos)
                    lastItemSelectedPos = selectedItemPos
                    items.forEach{ it.selected = false }
                    items[adapterPosition].selected = true
                }
                notifyItemChanged(selectedItemPos)
                return@setOnLongClickListener true
            }

            itemView.gift_idea_card_view.setOnClickListener {
                if(selectedItemPos != -1) {
                    if(items[selectedItemPos].selected) {
                        deselect(items[selectedItemPos])
                        selectedItemPos = -1
                    }
                    notifyItemChanged(selectedItemPos)
                } else {
                    selectedItemPos = adapterPosition
                    giftIdeaClickListener.onGiftItemClicked(adapterPosition)
                    selectedItemPos = -1
                }
            }

        }

        private suspend fun setCreatorName(giftIdea: GiftIdeaUIWrapper) {
            UserController().getUserById(giftIdea.ownerId).collect { state ->
                when(state) {
                    is State.Loading -> {}
                    is State.Success -> { creator.text = state.data.displayName }
                    is State.Failed -> {}
                }
            }
        }

        fun select(giftIdea: GiftIdeaUIWrapper) {
            itemView.gift_idea_background.setBackgroundResource(R.drawable.gift_idea_background_selected)
            giftIdea.selected = true
            giftIdeaClickListener.onGiftIdeaSelected(adapterPosition)
        }

        fun deselect(giftIdea: GiftIdeaUIWrapper) {
            itemView.gift_idea_background.setBackgroundResource(R.drawable.gift_idea_background)
            giftIdea.selected = false
            giftIdeaClickListener.onGiftIdeaDeselected(adapterPosition)
        }

    }

    interface GiftIdeaClickListener {
        fun onGiftItemClicked(position: Int)
        fun onGiftIdeaSelected(position: Int)
        fun onGiftIdeaDeselected(position: Int)
    }

}