package com.example.crypto.view_models

import com.example.crypto.repository.FakeDetailsScreenRepository
import com.example.crypto.utils.Resource
import com.example.crypto.views.fragments.details_screen.DetailsScreenContract
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class DetailsScreenViewModelTest :
    BaseViewModelTest<DetailsScreenContract.Event, DetailsScreenContract.State, DetailsScreenContract.Effect, DetailsScreenViewModel>() {


    @Test
    fun testDetailsScreenViewModel() = test(
        viewModel = DetailsScreenViewModel(FakeDetailsScreenRepository(), "testCoin"),
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
                    Resource.success(
                        mutableListOf()
                    )
                )
            ),
            DetailsScreenContract.State(
                DetailsScreenContract.ChartState.PerWeek(
                    Resource.success(
                        mutableListOf()
                    )
                )
            ),
            DetailsScreenContract.State(
                DetailsScreenContract.ChartState.PerMonth(
                    Resource.success(
                        mutableListOf()
                    )
                )
            ),
            DetailsScreenContract.State(
                DetailsScreenContract.ChartState.PerYear(
                    Resource.success(
                        mutableListOf()
                    )
                )
            ),
            DetailsScreenContract.State(
                DetailsScreenContract.ChartState.AllTime(
                    Resource.success(
                        mutableListOf()
                    )
                )
            )
        )
    )
}