package com.example.crypto.viewModels

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.repository.FakeMainScreenRepository
import com.example.crypto.repository.FakeSortPreferencesRepository
import com.example.crypto.repository.NETWORK_PAGE_SIZE
import com.example.crypto.utils.TestPagingSource
import com.example.crypto.utils.testFlow
import com.example.crypto.views.fragments.mainScreen.MainScreenContract
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class MainScreenViewModelTest() :
    BaseViewModelTest<MainScreenContract.Event, MainScreenContract.State, MainScreenContract.Effect, MainScreenViewModel>() {

//    val pager = Pager(
//        config = PagingConfig(
//            pageSize = NETWORK_PAGE_SIZE,
//            initialLoadSize = NETWORK_PAGE_SIZE,
//            enablePlaceholders = false
//        ),
//        pagingSourceFactory = { TestPagingSource() }
//    ).flow

    private val pager = testFlow


    @Test
    fun testMainScreenViewModel() = test(
        viewModel = MainScreenViewModel(FakeMainScreenRepository(), FakeSortPreferencesRepository()),
        events = listOf(
            MainScreenContract.Event.ChoseSortingByMarketCap,
            MainScreenContract.Event.ChoseSortingByPrice,
            MainScreenContract.Event.ChoseSortingByVolatility,
            MainScreenContract.Event.FetchFromDb
        ),
        assertions = listOf(
            MainScreenContract.State(MainScreenContract.RecycleViewState.Loading),
            MainScreenContract.State(MainScreenContract.RecycleViewState.SortingByMarketCap(pager)),
            MainScreenContract.State(MainScreenContract.RecycleViewState.SortingByPrice(pager)),
            MainScreenContract.State(MainScreenContract.RecycleViewState.SortingByVolatility(pager)),
            MainScreenContract.State(MainScreenContract.RecycleViewState.ItemsFromDb(pager))
        )
    )
}