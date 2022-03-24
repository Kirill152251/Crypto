package com.example.crypto.viewModels

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.example.crypto.model.interfacesMVI.UiEffect
import com.example.crypto.model.interfacesMVI.UiEvent
import com.example.crypto.model.interfacesMVI.UiState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
abstract class BaseViewModelTest<Event : UiEvent, State : UiState, Effect : UiEffect, ViewModel : BaseViewModel<State, Event, Effect>> {

    private val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    fun test(
        viewModel: ViewModel,
        events: List<Event>,
        assertions: List<State>,
    ): Unit = runTest {
        val states = mutableListOf<State>()

        val stateCollectionJob =
            viewModel.viewModelScope.launch(UnconfinedTestDispatcher(TestCoroutineScheduler())) {
                viewModel.uiState.toList(states)
            }
        events.forEach { event -> viewModel.setEvent(event) }
        assertEquals(assertions.size, states.size)
        assertions.zip(states) { assertion, state ->
            assertEquals(assertion, state)
        }
        stateCollectionJob.cancel()
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }
}