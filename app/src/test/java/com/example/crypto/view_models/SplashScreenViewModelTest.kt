package com.example.crypto.view_models

import com.example.crypto.repository.interfaces.SplashScreenRepository
import com.example.crypto.views.fragments.splash_screen.SplashScreenContract
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class SplashScreenViewModelTest : BaseViewModelTest<SplashScreenContract.Event, SplashScreenContract.State, SplashScreenContract.Effect, SplashScreenViewModel>() {

    @Test
    fun testSplashScreenViewModel() {
        val repository = mockk<SplashScreenRepository>()
        val repositoryWithError = mockk<SplashScreenRepository>()
        coEvery { repository.fetchingAndCachingInitialCoins() } returns true
        coEvery { repositoryWithError.fetchingAndCachingInitialCoins() } returns false
        test(
            viewModel = SplashScreenViewModel(repository),
            events = listOf(SplashScreenContract.Event.LoadingInitialCoins),
            assertions = listOf(
                SplashScreenContract.State(SplashScreenContract.CachingInitialCoinsState.Loading),
                SplashScreenContract.State(SplashScreenContract.CachingInitialCoinsState.Success),
            )
        )
        test(
            viewModel = SplashScreenViewModel(repositoryWithError),
            events = listOf(SplashScreenContract.Event.LoadingInitialCoins),
            assertions = listOf(
                SplashScreenContract.State(SplashScreenContract.CachingInitialCoinsState.Loading),
                SplashScreenContract.State(SplashScreenContract.CachingInitialCoinsState.Error),
            )
        )
    }
}