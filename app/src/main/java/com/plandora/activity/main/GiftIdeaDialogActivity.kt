package com.plandora.activity.main

import com.plandora.models.gift_ideas.GiftIdeaUIWrapper

interface GiftIdeaDialogActivity {
    fun addGiftIdea(giftIdea: GiftIdeaUIWrapper)
    fun addGiftIdeasRecyclerView()
}