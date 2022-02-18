package com.example.crypto.views.fragments.mainScreen

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.crypto.model.api.responses.coinsList.Coin

class CoinsListAdapter(private val context: Context) : PagingDataAdapter<Coin, CoinsListViewHolder>(COIN_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsListViewHolder {
        return CoinsListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CoinsListViewHolder, position: Int) {
        val coin = getItem(position)
        holder.bind(coin!!, context)
    }
    companion object {
        private val COIN_COMPARATOR = object : DiffUtil.ItemCallback<Coin>() {
            override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean =
                oldItem == newItem
        }
    }

}