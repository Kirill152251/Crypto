package com.example.crypto.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto.model.interfaces_mvi.UiEffect
import com.example.crypto.model.interfaces_mvi.UiEvent
import com.example.crypto.model.interfaces_mvi.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect> : ViewModel() {

    val initialState : State by lazy { createInitialState() }
    abstract fun createInitialState() : State

    val currentState: State
        get() = uiState.value

    private val _uiState : MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _event : MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    private val _effect : Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()


    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            event.collect {
                handleEvent(it)
            }
        }
    }

    abstract fun handleEvent(event : Event)


    fun setEvent(event : Event) {
        val newEvent = event
        viewModelScope.launch { _event.emit(newEvent) }
    }


    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }


    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }
}