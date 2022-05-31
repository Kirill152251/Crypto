package com.example.crypto.view_models

import com.example.crypto.repository.interfaces.SplashScreenRepository
import com.example.crypto.views.fragments.splash_screen.SplashScreenContract.*
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

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class SplashScreenViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repositorySuccess: SplashScreenRepository
    private lateinit var repositoryError: SplashScreenRepository
    private lateinit var viewModelSuccess: SplashScreenViewModel
    private lateinit var viewModelWithError: SplashScreenViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repositorySuccess = mockk()
        coEvery { repositorySuccess.fetchAndCacheInitialCoins() } returns true
        viewModelSuccess = SplashScreenViewModel(repositorySuccess)

        repositoryError = mockk()
        coEvery { repositoryError.fetchAndCacheInitialCoins() } returns false
        viewModelWithError = SplashScreenViewModel(repositoryError)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial state`() {
        val actual = viewModelSuccess.currentState

        val expected = State(CachingInitialCoinsState.Loading)

        assertEquals(expected, actual)
    }

    @Test
    fun `test LoadingInitialCoins Event`() {
        viewModelWithError.setEvent(Event.LoadingInitialCoins)
        viewModelSuccess.setEvent(Event.LoadingInitialCoins)

        val actualSuccess = viewModelSuccess.currentState
        val actualError = viewModelWithError.currentState

        val expectedSuccess = State(CachingInitialCoinsState.Success)
        val expectedError = State(CachingInitialCoinsState.Error)

        assertEquals(expectedSuccess, actualSuccess)
        assertEquals(expectedError, actualError)
    }
}