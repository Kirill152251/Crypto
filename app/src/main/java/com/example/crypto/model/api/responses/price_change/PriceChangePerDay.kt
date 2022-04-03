package com.example.crypto.model.api.responses.price_change


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceChangePerDay(
    @SerialName("prices")
    val prices: List<List<Double>>
)