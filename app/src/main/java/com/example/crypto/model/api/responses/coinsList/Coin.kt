package com.example.crypto.model.api.responses.coinsList


import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import com.example.crypto.views.fragments.mainScreen.MainScreenContract
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "coins_table")
data class Coin(
    @SerialName("current_price")
    val currentPrice: String,

    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
    val coinId: String,

    @SerialName("image")
    val image: String,

    @SerialName("name")
    val name: String,

    @SerialName("symbol")
    val symbol: String,

    @SerialName("market_cap_rank")
    val marketCapRank: Int,

    @SerialName("market_cap")
    val marketCapValue: String,

    @SerialName("price_change_percentage_24h")
    val volatility: Double
)