package com.example.crypto.views.fragments.settings_screen

import com.example.crypto.model.interfaces_mvi.UiEffect
import com.example.crypto.model.interfaces_mvi.UiEvent
import com.example.crypto.model.interfaces_mvi.UiState
import com.example.crypto.model.settings_db.SettingsUserInfo

class SettingsScreenContract {

    sealed class Event : UiEvent {
        data class SaveUserInfo(val userInfo: SettingsUserInfo) : Event()
        object FetchUserInfo : Event()
    }

    sealed class State : UiState {
        data class FilledSettings(val data: SettingsUserInfo?) : State()
        object InitialSettings : State()
    }

    sealed class Effect : UiEffect {
        object ErrorToSavePhoto : Effect()
    }
}