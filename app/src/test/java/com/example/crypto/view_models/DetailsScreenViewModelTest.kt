package com.example.crypto.view_models

import com.example.crypto.model.constans.*
import com.example.crypto.repository.interfaces.DetailsScreenRepository
import com.example.crypto.utils.ApiResourceForPriceCharts
import com.example.crypto.views.fragments.details_screen.DetailsScreenContract.*
import com.github.mikephil.charting.data.Entry
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class DetailsScreenViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: DetailsScreenRepository
    private lateinit var viewModel: DetailsScreenViewModel
    private lateinit var mockResponse: ApiResourceForPriceCharts.Success<MutableList<Entry>>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        mockResponse = ApiResourceForPriceCharts.Success(mutableListOf(), "0", "0")
        coEvery { repository.fetchPriceChange("id", LABEL_DAY) } returns mockResponse
        coEvery { repository.fetchPriceChange("id", LABEL_MONTH) } returns mockResponse
        coEvery { repository.fetchPriceChange("id", LABEL_WEEK) } returns mockResponse
        coEvery { repository.fetchPriceChange("id", LABEL_YEAR) } returns mockResponse
        coEvery { repository.fetchPriceChange("id", LABEL_ALL_TIME) } returns mockResponse
        viewModel = DetailsScreenViewModel(repository, "id")
    }

    @Test
    fun `test initial state`() {
        val actual = viewModel.currentState

        val expected = State(ChartState.Loading)

        assertEquals(expected, actual)
    }

    @Test
    fun `test ChoseOneDayInterval Event`() {
        viewModel.setEvent(Event.ChoseOneDayInterval)

        val actual = viewModel.currentState

        val expected = State(ChartState.PerDay(mockResponse))

        assertEquals(expected, actual)
    }

    @Test
    fun `test ChoseOneWeekInterval Event`() {
        viewModel.setEvent(Event.ChoseOneWeekInterval)

        val actual = viewModel.currentState

        val expected = State(ChartState.PerWeek(mockResponse))

        assertEquals(expected, actual)
    }

    @Test
    fun `test ChoseOneMonthInterval Event`() {
        viewModel.setEvent(Event.ChoseOneMonthInterval)

        val actual = viewModel.currentState

        val expected = State(ChartState.PerMonth(mockResponse))

        assertEquals(expected, actual)
    }

    @Test
    fun `test ChoseOneYearInterval Event`() {
        viewModel.setEvent(Event.ChoseOneYearInterval)

        val actual = viewModel.currentState

        val expected = State(ChartState.PerYear(mockResponse))

        assertEquals(expected, actual)
    }

    @Test
    fun `test ChoseAllTimeInterval Event`() {
        viewModel.setEvent(Event.ChoseAllTimeInterval)

        val actual = viewModel.currentState

        val expected = State(ChartState.AllTime(mockResponse))

        assertEquals(expected, actual)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }
}