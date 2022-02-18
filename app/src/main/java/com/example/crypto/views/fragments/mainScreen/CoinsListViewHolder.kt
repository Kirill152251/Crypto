package com.example.crypto.views.fragments.mainScreen

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crypto.R
import com.example.crypto.model.api.responses.coinsList.Coin

class CoinsListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val coinPrice: TextView = view.findViewById(R.id.coin_price)
    private val coinName: TextView = view.findViewById(R.id.coin_name)
    private val coinSymbol: TextView = view.findViewById(R.id.coin_symbol)
    private val coinImage: ImageView = view.findViewById(R.id.coin_image)

    private var coin: Coin? = null

    init {
        view.setOnClickListener {
            // TODO: переход на Details screen
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(coin: Coin, context: Context) {
        coinPrice.text = coin.currentPrice.toString() + "$"
        coinSymbol.text = coin.symbol
        coinName.text = coin.name
        Glide.with(context).load(coin.image).into(coinImage)
    }
    companion object {
        fun create(parent: ViewGroup): CoinsListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.coins_list_item, parent, false)
            return CoinsListViewHolder(view)
        }
    }
}

