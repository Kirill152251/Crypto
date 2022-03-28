package com.example.crypto.model.api.responses.priceChange

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceChangePerDay(
    @SerialName("prices")
    val prices: List<List<Double>>
)