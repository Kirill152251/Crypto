package com.example.crypto.model.api.responses.priceChange

import com.google.gson.annotations.SerializedName

data class PriceChangePerDay(
    @SerializedName("prices")
    val prices: List<List<Double>>
)