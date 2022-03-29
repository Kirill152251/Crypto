package com.example.crypto.view_models

import com.example.crypto.repository.FakeSplashScreenRepository
import com.example.crypto.views.fragments.splash_screen.SplashScreenContract
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class SplashScreenViewModelTest : BaseViewModelTest<SplashScreenContract.Event, SplashScreenContract.State, SplashScreenContract.Effect, SplashScreenViewModel>() {

    @Test
    fun testSplashScreenViewModel() = test(
        viewModel = SplashScreenViewModel(FakeSplashScreenRepository()),
        events = listOf(SplashScreenContract.Event.LoadingInitialCoins),
        assertions = listOf(
            SplashScreenContract.State(SplashScreenContract.CachingInitialCoinsState.Loading),
            SplashScreenContract.State(SplashScreenContract.CachingInitialCoinsState.Success),
        )
    )
}