package com.example.crypto.views.fragments.settings_screen

import android.graphics.Bitmap
import com.example.crypto.model.interfaces_mvi.UiEffect
import com.example.crypto.model.interfaces_mvi.UiEvent
import com.example.crypto.model.interfaces_mvi.UiState
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.utils.Avatar

class SettingsScreenContract {

    sealed class Event : UiEvent {
        data class SaveUserInfo(val userInfo: SettingsUserInfo) : Event()
        object FetchUserInfo : Event()
        object DeleteAvatar: Event()
        data class SaveAvatar(val bitmap: Bitmap) : Event()
    }

    sealed class State : UiState {
        data class FilledSettings(val data: SettingsUserInfo?, val avatar: List<Avatar>) : State()
        object IdleState : State()
    }

    sealed class Effect : UiEffect {
        object ErrorToSavePhoto : Effect()
    }
}