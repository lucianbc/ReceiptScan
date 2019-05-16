package com.lucianbc.receiptscan.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(): ViewModel() {
    sealed class State {
        object Drafts: State()
        object Receipts: State()
    }

    val state = MutableLiveData<State>(State.Drafts)

    fun toDrafts() { to(State.Drafts) }
    fun toReceipts() { to(State.Receipts) }

    private fun to(it: State) { if (state.value != it) state.value = it }

    fun isDrafts(state: State) = state == State.Drafts
    fun isReceipts(state: State) = state == State.Receipts
}