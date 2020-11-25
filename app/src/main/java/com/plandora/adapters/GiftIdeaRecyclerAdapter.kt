package com.plandora.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.plandora.R
import com.plandora.controllers.PlandoraUserController
import com.plandora.models.GiftIdea
import kotlinx.android.synthetic.main.layout_gift_ideas_list_item.view.*

class GiftIdeaRecyclerAdapter(
    private var items: List<GiftIdea>,
    private var giftIdeaClickListener: GiftIdeaClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GiftIdeaViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.layout_gift_ideas_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is GiftIdeaViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getSelectedItems(): ArrayList<GiftIdea> {
        val selectedItems = ArrayList<GiftIdea>()
        for(giftIdea: GiftIdea in items) {
            if(giftIdea.selected) selectedItems.add(giftIdea)
        }
        return selectedItems
    }

    inner class GiftIdeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.gift_idea_title
        private val creator: TextView = itemView.gift_idea_creator
        private val ratingBar: RatingBar = itemView.gift_idea_rating

        fun bind(giftIdea: GiftIdea) {
            title.text = giftIdea.title
            creator.text = PlandoraUserController().getUserFromId(giftIdea.ownerId).displayName
            ratingBar.rating = giftIdea.rating

            when(giftIdea.selected) {
                true -> itemView.gift_idea_background.setBackgroundResource(R.drawable.gift_idea_background_selected)
                false -> itemView.gift_idea_background.setBackgroundResource(R.drawable.gift_idea_background)
            }

            itemView.gift_idea_card_view.setOnClickListener {
                when(giftIdea.selected) {
                    true -> deselect(giftIdea)
                    false -> select(giftIdea)
                }
            }
        }

        private fun select(giftIdea: GiftIdea) {
            itemView.gift_idea_background.setBackgroundResource(R.drawable.gift_idea_background_selected)
            giftIdea.selected = true
            giftIdeaClickListener.onGiftItemClicked(true)
        }

        private fun deselect(giftIdea: GiftIdea) {
            itemView.gift_idea_background.setBackgroundResource(R.drawable.gift_idea_background)
            giftIdea.selected = false
            if(getSelectedItems().isEmpty()) {
                giftIdeaClickListener.onGiftItemClicked(false)
            }
        }
    }

    interface GiftIdeaClickListener {
        fun onGiftItemClicked(activated: Boolean)
    }

}