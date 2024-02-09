package com.example.vp2

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vp2.ui.dashboard.DashboardFragment
import com.example.vp2.ui.home.HomeFragment
import com.example.vp2.ui.notifications.NotificationsFragment


const val HOME_FRAGMENT = "home_fragment"
const val NOTIFICATION_FRAGMENT = "notification_fragment"
const val DASHBOARD_FRAGMENT = "dashboard_fragment"

class StartAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val fragments: MutableMap<String, Fragment> = mutableMapOf()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<BottomNavEntry>) {
        val updatedFragments = items.associate { item ->
            val key = when (item) {
                BottomNavEntry.HOME -> HOME_FRAGMENT
                BottomNavEntry.DASHBOARD -> DASHBOARD_FRAGMENT
                BottomNavEntry.NOTIFICATIONS -> NOTIFICATION_FRAGMENT
            }
            key to when (item) {
                BottomNavEntry.HOME -> fragments.getOrPut(key) { HomeFragment() }
                BottomNavEntry.DASHBOARD -> fragments.getOrPut(key) { DashboardFragment() }
                BottomNavEntry.NOTIFICATIONS -> fragments.getOrPut(key) { NotificationsFragment() }
            }
        }

        val keysToRemove = fragments.keys - updatedFragments.keys

        keysToRemove.forEach { key ->
            fragments.remove(key)
        }

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(
        position: Int
    ): Fragment = when (position) {
        0 -> fragments.getOrDefault(key = HOME_FRAGMENT, defaultValue = HomeFragment())
        1 -> fragments.getOrDefault(key = DASHBOARD_FRAGMENT, defaultValue = DashboardFragment())
        2 -> fragments.getOrDefault(key = NOTIFICATION_FRAGMENT, defaultValue = NotificationsFragment())
        else -> error("invalid")
    }
}
