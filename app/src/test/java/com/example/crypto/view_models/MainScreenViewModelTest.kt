package com.example.crypto.view_models

import androidx.paging.PagingData
import com.example.crypto.repository.interfaces.MainScreenRepository
import com.example.crypto.repository.interfaces.SortPreferencesRepository
import com.example.crypto.views.fragments.main_screen.MainScreenContract.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class MainScreenViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var mainRepository: MainScreenRepository
    private lateinit var prefRepository: SortPreferencesRepository
    private lateinit var viewModel: MainScreenViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mainRepository = mockk()
        every { mainRepository.getCoinsFromDB() } returns emptyFlow()
        every { mainRepository.getCoinsByVol() } returns emptyFlow()
        every { mainRepository.getCoinsByPrice() } returns emptyFlow()
        every { mainRepository.getCoinsByCap() } returns emptyFlow()
        prefRepository = mockk()
        coEvery { prefRepository.getOrder() } returns "test"
        coEvery { prefRepository.saveOrder("test") } returns Unit
        viewModel = MainScreenViewModel(mainRepository, prefRepository)
    }
    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial state`() {
        val actual = viewModel.currentState

        val expected = State(recycleViewState = RecycleViewState.Loading)

        assertEquals(expected, actual)
    }

    @Test
    fun `test ChoseSortingByPrice Event`() {

        viewModel.setEvent(Event.ChoseSortingByPrice)

        val actual = viewModel.currentState
        val expected = State(recycleViewState = RecycleViewState.SortingByPrice(PagingData.empty()))

        assertEquals(expected, actual)
    }

    @Test
    fun `test ChoseSortingByVolatility Event`() {

        viewModel.setEvent(Event.ChoseSortingByVolatility)

        val actual = viewModel.currentState
        val expected = State(recycleViewState = RecycleViewState.SortingByVolatility(PagingData.empty()))

        assertEquals(expected, actual)
    }

    @Test
    fun `test ChoseSortingByMarketCap Event`() {

        viewModel.setEvent(Event.ChoseSortingByMarketCap)

        val actual = viewModel.currentState
        val expected = State(recycleViewState = RecycleViewState.SortingByMarketCap(PagingData.empty()))

        assertEquals(expected, actual)
    }

    @Test
    fun `test FetchFromDb Event`() {

        viewModel.setEvent(Event.FetchFromDb)

        val actual = viewModel.currentState
        val expected = State(recycleViewState = RecycleViewState.ItemsFromDb(PagingData.empty()))

        assertEquals(expected, actual)
    }

    @Test
    fun `test SaveSortingType Event`() {

        viewModel.setEvent(Event.SaveSortingType("test"))

        val actual = viewModel.currentState
        val expected = State(recycleViewState = RecycleViewState.IdleState)

        assertEquals(expected, actual)
    }
}