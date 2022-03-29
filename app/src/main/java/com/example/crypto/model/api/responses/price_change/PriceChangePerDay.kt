package com.example.crypto.model.api.responses.price_change

import com.google.gson.annotations.SerializedName

//@Serializable
data class PriceChangePerDay(
    //@SerialName("prices")
    @SerializedName("prices")
    val prices: List<List<Double>>
)