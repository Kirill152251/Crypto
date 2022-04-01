package com.example.crypto.view_models

import androidx.lifecycle.viewModelScope
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.repository.UserInfoRepositoryImpl
import com.example.crypto.views.fragments.settings_screen.SettingsScreenContract.State
import com.example.crypto.views.fragments.settings_screen.SettingsScreenContract.Event
import com.example.crypto.views.fragments.settings_screen.SettingsScreenContract.Effect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val repository: UserInfoRepositoryImpl
) : BaseViewModel<State, Event, Effect>() {

    override fun createInitialState(): State {
        return State.InitialSettings
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.SaveUserInfo -> insertInfo(event.userInfo)
            Event.FetchUserInfo -> getInfo()
        }
    }

    private fun insertInfo(settingsUserInfo: SettingsUserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUserInfo(settingsUserInfo)
        }
    }

    private fun getInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getUserInfo() ?: null
            setState { State.FilledSettings(data) }
        }
    }
}