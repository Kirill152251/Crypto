package com.example.crypto.views.fragments.main_screen

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crypto.R
import com.example.crypto.databinding.CoinsListItemBinding
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.utils.coinsPriceConverter

class CoinsListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val bindingRvItem = CoinsListItemBinding.bind(view)

    fun bind(coin: Coin, context: Context) {
        bindingRvItem.apply {
            textCoinPrice.text = context.getString(
                R.string.dollar_at_the_beginning,
                coinsPriceConverter(coin.currentPrice.toDouble())
            )
            textCoinSymbol.text = coin.symbol
            textCoinName.text = coin.name
            Glide.with(context).load(coin.image).into(imageCoin)
        }
    }
}

