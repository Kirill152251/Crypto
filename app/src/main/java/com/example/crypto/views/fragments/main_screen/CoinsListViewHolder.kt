package com.example.crypto.views.fragments.main_screen

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.crypto.databinding.CoinsListItemBinding
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.utils.priceConverterWithDollarChar

class CoinsListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val bindingRvItem = CoinsListItemBinding.bind(view)

    fun bind(coin: Coin) {
        bindingRvItem.apply {
            textCoinPrice.text = coin.currentPrice.priceConverterWithDollarChar()
            textCoinSymbol.text = coin.symbol
            textCoinName.text = coin.name
            imageCoin.load(coin.image)
        }
    }
}

