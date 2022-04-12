package com.example.crypto.view_models

import androidx.paging.PagingData
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.repository.interfaces.MainScreenRepository
import com.example.crypto.repository.interfaces.SortPreferencesRepository
import com.example.crypto.views.fragments.main_screen.MainScreenContract
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import org.junit.Test

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class MainScreenViewModelTest() :
    BaseViewModelTest<MainScreenContract.Event, MainScreenContract.State, MainScreenContract.Effect, MainScreenViewModel>() {

    val testFlow = flow<PagingData<Coin>> { PagingData.empty<Coin>() }

    @Test
    fun testMainScreenViewModel() {
        val mainScreenMockRepository = mockk<MainScreenRepository>()
        every { mainScreenMockRepository.getCoinsByCap() } returns testFlow
        every { mainScreenMockRepository.getCoinsByPrice() } returns emptyFlow()
        every { mainScreenMockRepository.getCoinsByVol() } returns emptyFlow()
        every { mainScreenMockRepository.getCoinsFromDB() } returns emptyFlow()

        val sortPreferencesMockRepository = mockk<SortPreferencesRepository>()
        coEvery { sortPreferencesMockRepository.getOrder() } returns ""
        coEvery { sortPreferencesMockRepository.saveOrder("test") } returns Unit

        test(
            viewModel = MainScreenViewModel(mainScreenMockRepository, sortPreferencesMockRepository),
            events = listOf(
                MainScreenContract.Event.ChoseSortingByMarketCap,
                MainScreenContract.Event.ChoseSortingByPrice,
                MainScreenContract.Event.ChoseSortingByVolatility,
                MainScreenContract.Event.FetchFromDb,
                MainScreenContract.Event.SaveSortingType("test")
            ),
            assertions = listOf(
                MainScreenContract.State(MainScreenContract.RecycleViewState.Loading),
                MainScreenContract.State(MainScreenContract.RecycleViewState.SortingByMarketCap(
                    testFlow)),
                MainScreenContract.State(MainScreenContract.RecycleViewState.SortingByPrice(
                    emptyFlow())),
                MainScreenContract.State(MainScreenContract.RecycleViewState.SortingByVolatility(
                    emptyFlow())),
                MainScreenContract.State(MainScreenContract.RecycleViewState.ItemsFromDb(emptyFlow()))
            )
        )
    }
}