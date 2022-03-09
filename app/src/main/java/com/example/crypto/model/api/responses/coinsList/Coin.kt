package com.example.crypto.model.api.responses.coinsList


import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import com.example.crypto.views.fragments.mainScreen.MainScreenContract
import com.google.gson.annotations.SerializedName

@Entity(tableName = "coins_table")
data class Coin(
    @SerializedName("current_price")
    val currentPrice: String,

    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val coinId: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("market_cap_rank")
    val marketCapRank: Int,

    @SerializedName("market_cap")
    val marketCapValue: String,

    @SerializedName("price_change_percentage_24h")
    val volatility: Double
)