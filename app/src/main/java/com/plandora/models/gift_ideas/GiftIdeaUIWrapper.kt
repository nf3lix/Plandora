package com.plandora.models.gift_ideas

import com.plandora.models.PlandoraUser

data class GiftIdeaUIWrapper(
    val title: String = "",
    val description: String = "",
    val ownerId: String = "",
    val rating: Float = 0F,
    val votes: ArrayList<PlandoraUser> = ArrayList(),
    var selected: Boolean = false
) {

    companion object {
        fun createFromGiftIdea(giftIdea: GiftIdea, selected: Boolean = false): GiftIdeaUIWrapper {
            return GiftIdeaUIWrapper(giftIdea.title, giftIdea.description, giftIdea.ownerId, giftIdea.rating, giftIdea.votes, selected)
        }

        fun createGiftIdeaFromUIWrapper(wrapper: GiftIdeaUIWrapper): GiftIdea {
            return GiftIdea(wrapper.title, wrapper.description, wrapper.ownerId, wrapper.rating, wrapper.votes)
        }
    }

}