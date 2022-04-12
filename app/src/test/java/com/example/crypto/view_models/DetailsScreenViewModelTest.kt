package com.example.crypto.view_models

import com.example.crypto.model.constans.*
import com.example.crypto.repository.interfaces.DetailsScreenRepository
import com.example.crypto.utils.ApiResourceForPriceCharts
import com.example.crypto.views.fragments.details_screen.DetailsScreenContract
import com.github.mikephil.charting.data.Entry
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class DetailsScreenViewModelTest :
    BaseViewModelTest<DetailsScreenContract.Event, DetailsScreenContract.State, DetailsScreenContract.Effect, DetailsScreenViewModel>() {


    @Test
    fun testDetailsScreenViewModel() {
        val repository = mockk<DetailsScreenRepository>()
        val mockResponse = ApiResourceForPriceCharts.Success(mutableListOf<Entry>(),"0", "0")
        coEvery { repository.fetchPriceChange("id", LABEL_DAY) } returns mockResponse
        coEvery { repository.fetchPriceChange("id", LABEL_MONTH) } returns mockResponse
        coEvery { repository.fetchPriceChange("id", LABEL_WEEK) } returns mockResponse
        coEvery { repository.fetchPriceChange("id", LABEL_YEAR) } returns mockResponse
        coEvery { repository.fetchPriceChange("id", LABEL_ALL_TIME) } returns mockResponse
        val vm = DetailsScreenViewModel(repository, "id")
        test(
            viewModel = vm,
            events = listOf(
                DetailsScreenContract.Event.ChoseOneDayInterval,
                DetailsScreenContract.Event.ChoseOneWeekInterval,
                DetailsScreenContract.Event.ChoseOneMonthInterval,
                DetailsScreenContract.Event.ChoseOneYearInterval,
                DetailsScreenContract.Event.ChoseAllTimeInterval
            ),
            assertions = listOf(
                DetailsScreenContract.State(DetailsScreenContract.ChartState.Loading),
                DetailsScreenContract.State(
                    DetailsScreenContract.ChartState.PerDay(
                        mockResponse
                    )
                ),
                DetailsScreenContract.State(
                    DetailsScreenContract.ChartState.PerWeek(
                        mockResponse
                    )
                ),
                DetailsScreenContract.State(
                    DetailsScreenContract.ChartState.PerMonth(
                        mockResponse
                    )
                ),
                DetailsScreenContract.State(
                    DetailsScreenContract.ChartState.PerYear(
                        mockResponse
                    )
                ),
                DetailsScreenContract.State(
                    DetailsScreenContract.ChartState.AllTime(
                        mockResponse
                    )
                )
            )
        )
    }
}