package com.example.crypto.model.api.responses.coins_list

import com.example.crypto.model.db.CoinEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coin(

    @SerialName("current_price")
    val currentPrice: Double,

    @SerialName("id")
    val coinId: String,

    @SerialName("image")
    val image: String,

    @SerialName("name")
    val name: String,

    @SerialName("symbol")
    val symbol: String,

    @SerialName("market_cap_rank")
    val marketCapRank: Int?,

    @SerialName("market_cap")
    val marketCapValue: Double?,

    @SerialName("price_change_percentage_24h")
    val volatility: Float?
)

fun Coin.mapToEntity(): CoinEntity {
    return CoinEntity(
        currentPrice = this.currentPrice,
        coinId = this.coinId,
        image = this.image,
        name = this.name,
        symbol = this.symbol,
        marketCapRank = this.marketCapRank,
        marketCapValue = this.marketCapValue,
        volatility = this.volatility
    )
}