package com.example.crypto.utils

import androidx.paging.PagingData
import com.example.crypto.model.api.responses.coins_list.Coin
import kotlinx.coroutines.flow.emptyFlow

val testFlow = emptyFlow<PagingData<Coin>>()