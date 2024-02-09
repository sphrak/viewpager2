package com.example.vp2

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vp2.ui.dashboard.DashboardFragment
import com.example.vp2.ui.home.HomeFragment
import com.example.vp2.ui.notifications.NotificationsFragment


const val HOME_FRAGMENT_KEY = 0x1L
const val NOTIFICATION_FRAGMENT_KEY = 0x2L
const val DASHBOARD_FRAGMENT_KEY = 0x3L

class StartAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val fragments: MutableList<Pair<Long, Fragment>> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<BottomNavEntry>) {

        val updatedFragments: List<Pair<Long, Fragment>> = items.map { item: BottomNavEntry ->

            val key = when (item) {
                BottomNavEntry.HOME -> HOME_FRAGMENT_KEY
                BottomNavEntry.DASHBOARD -> DASHBOARD_FRAGMENT_KEY
                BottomNavEntry.NOTIFICATIONS -> NOTIFICATION_FRAGMENT_KEY
            }

            val fragment: Pair<Long, Fragment>? = fragments.find { it.first == key }

            key to when (item) {
                BottomNavEntry.HOME -> fragment?.second ?: HomeFragment()
                BottomNavEntry.DASHBOARD -> fragment?.second ?: DashboardFragment()
                BottomNavEntry.NOTIFICATIONS -> fragment?.second ?: NotificationsFragment()
            }
        }
        println("updated fragments = $updatedFragments")

        val keysToRemove: List<Pair<Long, Fragment>> = fragments - updatedFragments
        println("keys to remove = $keysToRemove")
        keysToRemove.forEach { key: Pair<Long, Fragment> ->
            fragments.remove(key)
        }

        updatedFragments.forEach {
            if (!fragments.contains(it)) {
                fragments.add(it)
            }
        }

        println("fragments: $fragments")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = fragments.size
    override fun getItemId(position: Int): Long {
        return fragments[position].first
    }
    override fun containsItem(itemId: Long): Boolean {
        return fragments.any {
            it.first == itemId
        }
    }

    // itemId -> pos
    fun itemIdByPosition(itemId: Long): Int {
        return fragments.indexOfFirst {
            it.first == itemId
        }
    }

    override fun createFragment(
        position: Int
    ): Fragment = fragments[position].second
}
