package com.example.crypto.model.api.responses.coins_list


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crypto.model.constans.COINS_DATABASE_NAME
import com.google.gson.annotations.SerializedName

//@Serializable
@Entity(tableName = COINS_DATABASE_NAME)
data class Coin(
    //@SerialName("current_price")
    @SerializedName("current_price")
    val currentPrice: String,

    //@SerialName("id")
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val coinId: String,

    //@SerialName("image")
    @SerializedName("image")
    val image: String,

    //@SerialName("name")
    @SerializedName("name")
    val name: String,

    //@SerialName("symbol")
    @SerializedName("symbol")
    val symbol: String,

    //@SerialName("market_cap_rank")
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,

    //@SerialName("market_cap")
    @SerializedName("market_cap")
    val marketCapValue: String,

    //@SerialName("price_change_percentage_24h")
    @SerializedName("price_change_percentage_24h")
    val volatility: Double
)