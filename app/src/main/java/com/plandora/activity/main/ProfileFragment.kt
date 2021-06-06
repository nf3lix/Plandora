package com.plandora.activity.main

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.plandora.R
import com.plandora.controllers.EventController
import com.plandora.controllers.State
import com.plandora.controllers.UserController
import com.plandora.models.PlandoraUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        displayProfileFragment(inflater, container)
        CoroutineScope(Dispatchers.Main).launch {
            addInformation()
        }
        return rootView


    }

    private fun displayProfileFragment(inflater: LayoutInflater, container: ViewGroup?) {
        rootView = inflater.inflate(R.layout.fragment_profile_main, container, false)
    }

    private suspend fun addInformation(){
        UserController().getUserById(UserController().currentUserId()).collect { state ->
            when(state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    fillInformation(state.data)
                }
                is State.Failed -> {
                }
            }
        }
    }

    private fun fillInformation(user: PlandoraUser){
        rootView.findViewById<TextView>(R.id.profile_username).text = user.displayName
        rootView.findViewById<TextView>(R.id.profile_username).movementMethod =
            ScrollingMovementMethod()

        rootView.findViewById<TextView>(R.id.profile_email).text = user.email
        rootView.findViewById<TextView>(R.id.profile_email).movementMethod =
            ScrollingMovementMethod()

        EventController().updateEventList()
        rootView.findViewById<TextView>(R.id.profile_event_counter).text = EventController.eventList.size.toString()

    }

}