package com.plandora.models

import com.plandora.models.gift_ideas.GiftIdea
import com.plandora.models.gift_ideas.GiftIdeaUIWrapper
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

class GiftIdeaUIWrapperUnitTest {

    private lateinit var giftIdea: GiftIdea
    private lateinit var giftIdeaUIWrapper: GiftIdeaUIWrapper

    @Before
    fun createUser() {
        giftIdea = GiftIdea("TITLE", "DESCRIPTION",
            "OWNER_ID", 3.5F)
    }

    @Test
    fun createFromGiftIdea_isCorrect() {
        val wrapper1 = GiftIdeaUIWrapper.createFromGiftIdea(giftIdea)
        compareGiftIdeas(giftIdea, wrapper1)
        assertEquals(wrapper1.selected, false)
        val wrapper2 = GiftIdeaUIWrapper.createFromGiftIdea(giftIdea, false)
        compareGiftIdeas(giftIdea, wrapper2)
        assertEquals(wrapper2.selected, false)
        val wrapper3 = GiftIdeaUIWrapper.createFromGiftIdea(giftIdea, true)
        compareGiftIdeas(giftIdea, wrapper3)
        assertEquals(wrapper3.selected, true)
    }

    private fun compareGiftIdeas(giftIdea: GiftIdea, wrapper: GiftIdeaUIWrapper) {
        assertEquals(wrapper.title, giftIdea.title)
        assertEquals(wrapper.description, giftIdea.description)
        assertEquals(wrapper.ownerId, giftIdea.ownerId)
        assertEquals(wrapper.rating, giftIdea.rating)
    }

}