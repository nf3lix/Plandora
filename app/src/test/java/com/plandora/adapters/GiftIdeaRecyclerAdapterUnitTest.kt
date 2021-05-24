package com.plandora.adapters

import com.plandora.activity.main.dashboard.EventDetailActivity
import com.plandora.models.gift_ideas.GiftIdeaUIWrapper
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GiftIdeaRecyclerAdapterUnitTest {

    private lateinit var adapter: GiftIdeaRecyclerAdapter
    private val giftIdeasList: ArrayList<GiftIdeaUIWrapper> = ArrayList()
    private val activity = EventDetailActivity()

    @Before
    fun initAdapter() {
        adapter = GiftIdeaRecyclerAdapter(giftIdeasList, activity, false)
    }

    @Test
    fun adapterItemCount_isCorrect() {
        val giftIdea = GiftIdeaUIWrapper()
        assertEquals(adapter.itemCount, 0)
        giftIdeasList.add(GiftIdeaUIWrapper())
        assertEquals(adapter.itemCount, 1)
        giftIdeasList.add(giftIdea)
        assertEquals(adapter.itemCount, 2)
        giftIdeasList.remove(giftIdea)
        assertEquals(adapter.itemCount, 1)
    }

    @Test
    fun getSelectedItems_isCorrect() {
        val ga1 = GiftIdeaUIWrapper(selected = true)
        val ga2 = GiftIdeaUIWrapper(selected = true)
        val ga3 = GiftIdeaUIWrapper(selected = false)
        giftIdeasList.addAll(arrayListOf(ga1, ga2, ga3))
        val selectedItems = adapter.getSelectedItems()
        assertTrue(selectedItems.contains(ga1))
        assertTrue(selectedItems.contains(ga2))
        assertTrue(selectedItems.size == 2)
    }

}