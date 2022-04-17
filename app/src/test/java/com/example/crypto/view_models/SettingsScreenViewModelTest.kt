package com.example.crypto.view_models

import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.repository.interfaces.UserInfoRepository
import com.example.crypto.views.fragments.settings_screen.SettingsScreenContract.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class SettingsScreenViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: UserInfoRepository
    private lateinit var viewModel: SettingsScreenViewModel
    private val testUserInfo = SettingsUserInfo("test", "test", "test")


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        coEvery { repository.getUserInfo() } returns testUserInfo
        coEvery { repository.insertUserInfo(testUserInfo) } returns Unit
        viewModel = SettingsScreenViewModel(repository)
    }

    @Test
    fun `test setting initial state`() {

        val actual = viewModel.currentState
        val expected = State.IdleState

        assertEquals(expected, actual)
    }

    @Test
    fun `test fetchUserInfo Event`() {

        viewModel.setEvent(Event.FetchUserInfo)

        val actual = viewModel.currentState
        val expected = State.FilledSettings(testUserInfo)

        assertEquals(expected, actual)
    }

    @Test
    fun `test save user info Event`() {

        viewModel.setEvent(Event.SaveUserInfo(testUserInfo))

        val actual = viewModel.currentState
        val expected = State.IdleState

        assertEquals(expected, actual)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }
}