package com.plandora.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.plandora.R
import com.plandora.models.GiftIdea
import kotlinx.android.synthetic.main.layout_gift_ideas_list_item.view.*

class GiftIdeaRecyclerAdapter(private var items: List<GiftIdea>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    class GiftIdeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.gift_idea_title
        private val creator: TextView = itemView.gift_idea_creator
        private val ratingBar: RatingBar = itemView.gift_idea_rating

        fun bind(giftIdea: GiftIdea) {
            title.text = giftIdea.title
            creator.text = giftIdea.ownerId // TODO: display name
            ratingBar.rating = giftIdea.rating
        }
    }

}