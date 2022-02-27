package com.example.crypto.views.fragments.mainScreen

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.crypto.R
import com.example.crypto.databinding.FragmentMainScreenBinding
import com.example.crypto.model.constans.*
import com.example.crypto.model.db.CoinsListDataBase
import com.example.crypto.viewModels.CoinsListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CoinsListViewModel by viewModel()
    private val database: CoinsListDataBase by inject()
    private val adapter: CoinsListAdapter by inject ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainScreenToolbar.inflateMenu(R.menu.sort_menu)

        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)

        lifecycleScope.launch(Dispatchers.IO) {
            database.coinsListDao().clearDb()
        }
        binding.rvCoins.addItemDecoration(decoration)
        //by default list sorted by market capitalization
        binding.rvCoins.adapter = adapter
        submitSortedDataIntoAdapter(QUERY_SORT_BY_MARKET_CAP)
        sortingByToolBarMenu()
        pullToRefresh()

        lifecycleScope.launch() {
            adapter.loadStateFlow.collect { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.Loading -> {
                        binding.apply {
                            mainScreenProgressbar.visibility = View.VISIBLE
                            rvCoins.visibility = View.GONE
                        }
                    }
                    else -> {
                        binding.apply {
                            rvCoins.visibility = View.VISIBLE
                            mainScreenProgressbar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun pullToRefresh() {
        binding.swipeToRefreshLayout.setOnRefreshListener {

            when (viewModel.currentSorting.value) {
                QUERY_SORT_BY_PRICE -> {
                    submitSortedDataIntoAdapter(QUERY_SORT_BY_PRICE)
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                QUERY_SORT_BY_MARKET_CAP -> {
                    submitSortedDataIntoAdapter(QUERY_SORT_BY_MARKET_CAP)
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                else -> {
                    submitSortedDataIntoAdapter(QUERY_SORT_BY_VOLATILITY)
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
            }

        }
    }

    private fun sortingByToolBarMenu() {
        binding.mainScreenToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sort_by_price -> {
                    submitSortedDataIntoAdapter(QUERY_SORT_BY_PRICE)
                    binding.rvCoins.smoothScrollToPosition(0)
                    true
                }
                R.id.sort_by_market_cap -> {
                    submitSortedDataIntoAdapter(QUERY_SORT_BY_MARKET_CAP)
                    binding.rvCoins.smoothScrollToPosition(0)
                    true
                }
                R.id.sort_by_volatility -> {
                    submitSortedDataIntoAdapter(QUERY_SORT_BY_VOLATILITY)
                    binding.rvCoins.smoothScrollToPosition(0)
                    true
                }
                else -> false
            }
        }
    }

    private fun submitSortedDataIntoAdapter(order: String) {
        lifecycleScope.launch() {
            when (order) {
                QUERY_SORT_BY_MARKET_CAP -> {
                    viewModel.getCoinsByMarketCap().distinctUntilChanged().collectLatest {
                        adapter.submitData(it)
                    }
                }
                QUERY_SORT_BY_PRICE -> {
                    viewModel.getCoinsByPrice().distinctUntilChanged().collectLatest {
                        adapter.submitData(it)
                    }
                }
                QUERY_SORT_BY_VOLATILITY -> {
                    viewModel.getCoinsByVolatility().distinctUntilChanged().collectLatest {
                        adapter.submitData(it)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}