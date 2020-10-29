package com.plandora.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.plandora.R
import com.plandora.activity.CreateEventActivity
import com.plandora.activity.PlandoraActivity
import com.plandora.activity.main.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : PlandoraActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addActionBar()
        fab_create_board.setOnClickListener { startActivity(Intent(this, CreateEventActivity::class.java)) }
        main_nav_view.setNavigationItemSelectedListener(this)
        updateFragmentViewById(R.id.bottom_nav_dashboard)
        bottom_navigation_view.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener updateFragmentViewById(it.itemId)
        }
    }

    private fun toggleDrawer() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    private fun addActionBar() {
        setSupportActionBar(toolbar_main_activity)
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_vector_burger_menu)
        toolbar_main_activity.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem) : Boolean {
        // TODO
        return true
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        return true
    }

    private fun updateFragmentViewById(menuItemId: Int): Boolean {
        when(menuItemId) {
            R.id.bottom_nav_dashboard -> {
                loadFragment(DashboardFragment())
                toolbar_main_activity.title = resources.getString(R.string.dashboard_toolbar_title)
                return true
            }
            R.id.bottom_nav_notifications -> {
                loadFragment(NotificationsFragment())
                toolbar_main_activity.title = resources.getString(R.string.notifications_toolbar_title)
                return true
            }
            R.id.bottom_nav_profile -> {
                loadFragment(ProfileFragment())
                toolbar_main_activity.title = resources.getString(R.string.profile_toolbar_title)
                return true
            }
        }
        return false
    }

}