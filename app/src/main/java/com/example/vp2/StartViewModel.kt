package com.example.vp2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


enum class BottomNavEntry {
    HOME,
    DASHBOARD,
    NOTIFICATIONS;
}

private val Online = buildList {
    add(BottomNavEntry.HOME)
    add(BottomNavEntry.DASHBOARD)
    add(BottomNavEntry.NOTIFICATIONS)
}

private val Offline = buildList {
    add(BottomNavEntry.HOME)
    add(BottomNavEntry.NOTIFICATIONS)
}

sealed interface State {
    data object Idle : State
    data class BottomNav(val items: List<BottomNavEntry>) : State
}

class StartViewModel : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    fun onClick() {
        println("on click")
        viewModelScope.launch {
            _state.update {
                State.BottomNav(
                    items = if (it is State.BottomNav && it.items.contains(BottomNavEntry.DASHBOARD)) {
                        Offline
                    } else {
                        Online
                    }
                )
            }
        }
    }

}