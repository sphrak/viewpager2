package com.example.vp2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.vp2.databinding.FragmentStartBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StartFragment : Fragment() {

    private lateinit var startAdapter: StartAdapter
    private val viewModel: StartViewModel = StartViewModel()

    private var _binding: FragmentStartBinding? = null

    private val binding get() = _binding!!
    private val viewPager get() = binding.viewpager

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.bottomNavigationView.menu.getItem(position).isChecked = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val v = FragmentStartBinding.inflate(inflater)
        _binding = v
        return v.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAdapter = StartAdapter(requireActivity())
        binding.viewpager.adapter = startAdapter

        viewPager.setPageTransformer(null)
        viewPager.isUserInputEnabled = false

        val bottomNavigationView = binding.bottomNavigationView

        binding.button.setOnClickListener {
            viewModel.onClick()
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> viewPager.setCurrentItem(0, false)
                R.id.navigation_dashboard -> viewPager.setCurrentItem(1, false)
                R.id.navigation_notifications -> viewPager.setCurrentItem(2, false)
            }
            true
        }

        // Sync ViewPager swipe with BottomNavigationView
        viewPager.registerOnPageChangeCallback(onPageChangeCallback)

        viewModel
            .state
            .onEach(::handleState)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.onClick()
    }

    private fun handleState(state: State) {
        when (state) {
            is State.BottomNav -> {
                startAdapter.submitList(state.items)
                // update menu
                binding.bottomNavigationView.setupMenu(state.items)

                if (startAdapter.itemCount > 0) {
                    viewPager.offscreenPageLimit = startAdapter.itemCount
                }
            }
            State.Idle -> Unit
        }
    }

    private val BottomNavEntry.id get() = when (this) {
        BottomNavEntry.HOME -> R.id.navigation_home
        BottomNavEntry.DASHBOARD -> R.id.navigation_dashboard
        BottomNavEntry.NOTIFICATIONS -> R.id.navigation_notifications
    }

    private val BottomNavEntry.title get() = when (this) {
        BottomNavEntry.HOME -> "Home"
        BottomNavEntry.DASHBOARD -> "Dashboard"
        BottomNavEntry.NOTIFICATIONS -> "Notifications"
    }

    private val BottomNavEntry.drawableRes: Int get() = when (this) {
        BottomNavEntry.HOME -> R.drawable.ic_home_black_24dp
        BottomNavEntry.DASHBOARD -> R.drawable.ic_dashboard_black_24dp
        BottomNavEntry.NOTIFICATIONS -> R.drawable.ic_notifications_black_24dp
    }

    private fun BottomNavigationView.setupMenu(items: List<BottomNavEntry>) {
        menu.clear()
        for (item in items) {
            menu.add(Menu.NONE, item.id, Menu.NONE, item.title)
                .setIcon(item.drawableRes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.viewpager.unregisterOnPageChangeCallback(onPageChangeCallback)
        binding.viewpager.adapter = null
        _binding = null
    }
}