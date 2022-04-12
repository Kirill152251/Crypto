package com.example.crypto.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.model.constans.COINS_DATABASE_NAME

@Entity(tableName = COINS_DATABASE_NAME)
data class CoinEntity(
    val currentPrice: Double,
    @PrimaryKey(autoGenerate = false)
    val coinId: String,

    val image: String,

    val name: String,

    val symbol: String,

    val marketCapRank: Int?,

    val marketCapValue: Double?,

    val volatility: Float?
)
fun CoinEntity.mapToCoin(): Coin {
    return Coin(
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
