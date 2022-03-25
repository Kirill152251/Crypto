package com.example.crypto.viewModels

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.example.crypto.repository.FakeDetailsScreenRepository
import com.example.crypto.utils.Resource
import com.example.crypto.views.fragments.detailsScreen.DetailsScreenContract
import com.example.crypto.views.fragments.splashScreen.SplashScreenContract
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
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