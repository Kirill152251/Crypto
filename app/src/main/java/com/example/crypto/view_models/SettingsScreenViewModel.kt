package com.example.crypto.view_models

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.example.crypto.model.constans.MAX_INPUT_SIZE
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.repository.interfaces.UserInfoRepository
import com.example.crypto.view_models.interfaces.SettingsScreenVM
import com.example.crypto.views.fragments.settings_screen.SettingsScreenContract.State
import com.example.crypto.views.fragments.settings_screen.SettingsScreenContract.Event
import com.example.crypto.views.fragments.settings_screen.SettingsScreenContract.Effect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val repository: UserInfoRepository
) : BaseViewModel<State, Event, Effect>(), SettingsScreenVM {

    override fun createInitialState(): State {
        return State.IdleState
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.SaveUserInfo -> insertInfo(event.userInfo)
            Event.FetchUserInfo -> getInfo()
            is Event.SaveAvatar -> saveAvatar(event.bitmap)
        }
    }

    override fun saveAvatar(bitmap: Bitmap?) {
        viewModelScope.launch {
            bitmap?.let {
                repository.deleteAvatar()
                val isSavedSuccessfully = repository.saveAvatar(bitmap)
                if (isSavedSuccessfully) {
                    getInfo()
                } else {
                    setEffect { Effect.ErrorToSavePhoto }
                }
            } ?: setEffect { Effect.ErrorToSavePhoto }
        }
    }

    override fun insertInfo(settingsUserInfo: SettingsUserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUserInfo(settingsUserInfo)
        }
        setState { State.IdleState }
    }

    override fun getInfo() {
        viewModelScope.launch {
            val data = repository.getUserInfo()
            val avatar = repository.getAvatar()
            setState { State.FilledSettings(data, avatar) }
        }
    }

    override fun isInputValid(firstName: String, lastName: String): Boolean {
        return !(firstName.isEmpty() || lastName.isEmpty()
                || lastName.length > MAX_INPUT_SIZE
                || firstName.length > MAX_INPUT_SIZE)
    }
}