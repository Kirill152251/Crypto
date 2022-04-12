package com.example.crypto.views.fragments.details_screen

import com.example.crypto.model.interfaces_mvi.UiEffect
import com.example.crypto.model.interfaces_mvi.UiEvent
import com.example.crypto.model.interfaces_mvi.UiState
import com.example.crypto.utils.ApiResourceForPriceCharts
import com.github.mikephil.charting.data.Entry

class DetailsScreenContract {

    sealed class Event : UiEvent {
        object ChoseOneDayInterval : Event()
        object ChoseOneWeekInterval : Event()
        object ChoseOneMonthInterval : Event()
        object ChoseOneYearInterval : Event()
        object ChoseAllTimeInterval : Event()
    }

    data class State(
        val chartState: ChartState
    ) : UiState

    sealed class ChartState {
        object Loading : ChartState()
        data class PerDay(val priceData: ApiResourceForPriceCharts<List<Entry>>) : ChartState()
        data class PerWeek(val priceData: ApiResourceForPriceCharts<List<Entry>>) : ChartState()
        data class PerMonth(val priceData: ApiResourceForPriceCharts<List<Entry>>) : ChartState()
        data class PerYear(val priceData: ApiResourceForPriceCharts<List<Entry>>) : ChartState()
        data class AllTime(val priceData: ApiResourceForPriceCharts<List<Entry>>) : ChartState()
    }

    sealed class Effect : UiEffect {}
}