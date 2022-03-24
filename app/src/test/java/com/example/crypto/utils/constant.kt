package com.example.crypto.utils

import androidx.paging.PagingData
import com.example.crypto.model.api.responses.coinsList.Coin
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow

val testFlow = emptyFlow<PagingData<Coin>>()