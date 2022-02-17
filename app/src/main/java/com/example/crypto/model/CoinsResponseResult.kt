package com.example.crypto.model

import com.example.crypto.model.api.responses.coinsList.Coin
import java.lang.Exception

sealed class CoinsResponseResult{
    data class Success(val data: List<Coin>) : CoinsResponseResult()
    data class Error(val error: Exception) : CoinsResponseResult()
}
