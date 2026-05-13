package com.example.nammashaaleinventory.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MutableSession {
    private val _state = MutableStateFlow(false to "")
    val state: StateFlow<Pair<Boolean, String>> = _state

    fun login(email: String) {
        _state.value = true to email
    }

    fun logout() {
        _state.value = false to ""
    }
}
