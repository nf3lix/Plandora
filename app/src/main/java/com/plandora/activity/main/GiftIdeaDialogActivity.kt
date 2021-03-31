package com.plandora.activity.main

import com.plandora.models.gift_ideas.GiftIdea

interface GiftIdeaDialogActivity {
    fun addGiftIdea(giftIdea: GiftIdea)
    fun addGiftIdeasRecyclerView()
}