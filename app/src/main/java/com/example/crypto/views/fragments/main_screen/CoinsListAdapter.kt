package com.example.crypto.views.fragments.main_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.crypto.R
import com.example.crypto.databinding.CoinsListItemBinding
import com.example.crypto.model.api.responses.coins_list.Coin

class CoinsListAdapter(
    private val clickListener: (coinItem: Coin, binding: CoinsListItemBinding) -> Unit
) : PagingDataAdapter<Coin, CoinsListViewHolder>(COIN_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.coins_list_item, parent, false)
        return CoinsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinsListViewHolder, position: Int) {
        val coin = getItem(position)
        holder.bind(coin!!)
        holder.itemView.setOnClickListener { clickListener(coin, holder.bindingRvItem) }
    }

    companion object {
        private val COIN_COMPARATOR = object : DiffUtil.ItemCallback<Coin>() {
            override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean =
                oldItem.coinId == newItem.coinId

            override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean =
                oldItem == newItem
        }
    }
}