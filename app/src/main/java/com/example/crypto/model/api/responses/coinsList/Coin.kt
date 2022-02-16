package com.example.crypto.model.api.responses.coinsList


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "coins_table")
data class Coin(
    @SerializedName("current_price")
    val currentPrice: Double,

    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("symbol")
    val symbol: String,
)