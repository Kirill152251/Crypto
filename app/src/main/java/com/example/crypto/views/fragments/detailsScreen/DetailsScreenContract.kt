package com.example.crypto.views.fragments.detailsScreen

import androidx.paging.PagingData
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.model.interfacesMVI.UiEffect
import com.example.crypto.model.interfacesMVI.UiEvent
import com.example.crypto.model.interfacesMVI.UiState
import com.example.crypto.utils.Resource
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.flow.Flow

class DetailsScreenContract {

    sealed class Event: UiEvent {
        object ChoseOneDayInterval: Event()
        object ChoseOneWeekInterval: Event()
        object ChoseOneMonthInterval: Event()
        object ChoseOneYearInterval: Event()
        object ChoseAllTimeInterval: Event()
    }

    data class State(
        val chartState: ChartState
    ): UiState

    sealed class ChartState {
        object Loading: ChartState()
        data class PerDay(val priceData: Resource<List<Entry>>): ChartState()
        data class PerWeek(val priceData: Resource<List<Entry>>): ChartState()
        data class PerMonth(val priceData: Resource<List<Entry>>): ChartState()
        data class PerYear(val priceData: Resource<List<Entry>>): ChartState()
        data class AllTime(val priceData: Resource<List<Entry>>): ChartState()
    }

    sealed class Effect: UiEffect {}
}