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
        val adapter = CoinsListAdapter(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {
            database.coinsListDao().clearDb()
        }
        binding.rvCoins.addItemDecoration(decoration)
        //by default list sorted by market capitalization
        binding.rvCoins.adapter = adapter
        submitSortedDataIntoAdapter(QUERY_SORT_BY_MARKET_CAP, adapter)
        sortingByToolBarMenu()

        lifecycleScope.launch() {
            adapter.loadStateFlow.collect { loadState ->
                when(loadState.source.refresh) {
                    is LoadState.Loading -> {
                        binding.apply {
                            when ( val drawable = splashScreenAnim.drawable) {
                                is AnimatedVectorDrawableCompat -> {
                                    drawable.start()
                                }
                                is AnimatedVectorDrawable -> {
                                    drawable.start()
                                }
                            }
                            splashScreenAnim.visibility = View.VISIBLE
                            mainScreenToolbar.visibility = View.GONE
                            rvCoins.visibility = View.GONE
                        }
                    }
                    else -> {
                        binding.apply {
                            rvCoins.visibility = View.VISIBLE
                            mainScreenToolbar.visibility = View.VISIBLE
                            splashScreenAnim.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun sortingByToolBarMenu() {
        val newAdapter = CoinsListAdapter(requireContext())
        binding.mainScreenToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sort_by_price -> {
                    binding.rvCoins.adapter = newAdapter
                    submitSortedDataIntoAdapter(QUERY_SORT_BY_PRICE, newAdapter)
                    binding.rvCoins.smoothScrollToPosition(0)
                    true
                }
                R.id.sort_by_market_cap -> {
                    binding.rvCoins.adapter = newAdapter
                    submitSortedDataIntoAdapter(QUERY_SORT_BY_MARKET_CAP, newAdapter)
                    binding.rvCoins.smoothScrollToPosition(0)
                    true
                }
                R.id.sort_by_volatility -> {
                    binding.rvCoins.adapter = newAdapter
                    submitSortedDataIntoAdapter(QUERY_SORT_BY_VOLATILITY, newAdapter)
                    binding.rvCoins.smoothScrollToPosition(0)
                    true
                }
                else -> false
            }
        }
    }
    private fun submitSortedDataIntoAdapter(order: String, adapter: CoinsListAdapter) {
        lifecycleScope.launch() {
            when(order) {
                QUERY_SORT_BY_MARKET_CAP -> {
                    viewModel.getCoins(order).distinctUntilChanged().collectLatest {
                        adapter.submitData(it)
                    }
                }
                QUERY_SORT_BY_PRICE -> {
                    viewModel.getCoins(order).distinctUntilChanged().collectLatest {
                        adapter.submitData(it)
                    }
                }
                QUERY_SORT_BY_VOLATILITY -> {
                    viewModel.getCoins(order).distinctUntilChanged().collectLatest {
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