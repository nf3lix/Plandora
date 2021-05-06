package com.plandora.activity.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.plandora.R
import com.plandora.controllers.InvitationController
import com.plandora.controllers.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {

    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications_main, container, false)
    }

    override fun onResume() {
        super.onResume()
        uiScope.launch {
            loadInvitations()
        }
    }

    private suspend fun loadInvitations() {
        InvitationController().updateInvitationList().collect { state ->
            when(state) {
                is State.Loading -> { }
                is State.Success -> { }
                is State.Failed -> {
                    Toast.makeText(activity, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}