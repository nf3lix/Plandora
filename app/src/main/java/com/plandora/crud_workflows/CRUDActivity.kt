package com.plandora.crud_workflows

import com.plandora.models.events.Event
import com.plandora.models.gift_ideas.GiftIdea

interface CRUDActivity {

    fun onInternalFailure(message: String)

    interface GiftIdeaCRUDActivity : CRUDActivity {
        fun onCreateSuccess(giftIdea: GiftIdea)
        fun onCreateFailure()
        fun onRemoveSuccess(giftIdea: GiftIdea)
        fun onRemoveFailure(message: String)

    }
    interface EventCRUDActivity : CRUDActivity {
        fun onCreateSuccess(event: Event)
        fun onCreateFailure()
        fun onUpdateSuccess(event: Event)
        fun onUpdateFailure(message: String)
        fun onRemoveSuccess(event: Event)
        fun onRemoveFailure(message: String)
    }

}