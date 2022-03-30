package com.example.crypto.views.fragments.main_screen

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crypto.R
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.utils.coinsPriceConverter

class CoinsListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val coinPrice: TextView = view.findViewById(R.id.text_coin_price)
    private val coinName: TextView = view.findViewById(R.id.text_coin_name)
    private val coinSymbol: TextView = view.findViewById(R.id.text_coin_symbol)
    private val coinImage: ImageView = view.findViewById(R.id.image_coin)


    @SuppressLint("SetTextI18n")
    fun bind(coin: Coin, context: Context) {
        coinPrice.text = "$ " + coinsPriceConverter(coin.currentPrice.toDouble())
        coinSymbol.text = coin.symbol
        coinName.text = coin.name
        Glide.with(context).load(coin.image).into(coinImage)
        itemView.setOnClickListener {
            val extras = FragmentNavigatorExtras(coinPrice to "price_details_screen")
            it.findNavController().navigate(
                MainScreenFragmentDirections.actionMainScreenFragmentToDetailsScreenFragment(
                    coinId = coin.coinId,
                    coinPrice = coin.currentPrice,
                    coinIconUrl = coin.image,
                    coinName = coin.name,
                    coinPriceChange = coin.volatility.toFloat(),
                    marketCap = coin.marketCapValue
                ),
                navigatorExtras = extras
            )
        }
    }

    companion object {
        fun create(parent: ViewGroup): CoinsListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.coins_list_item, parent, false)
            return CoinsListViewHolder(view)
        }
    }
}

