package com.example.crypto.views.fragments.main_screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.crypto.R
import com.example.crypto.databinding.FragmentMainScreenBinding
import com.example.crypto.model.constans.QUERY_SORT_BY_MARKET_CAP
import com.example.crypto.model.constans.QUERY_SORT_BY_PRICE
import com.example.crypto.model.constans.QUERY_SORT_BY_VOLATILITY
import com.example.crypto.utils.isOnline
import com.example.crypto.view_models.MainScreenViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainScreenViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvCoins.addItemDecoration(decoration)
        val adapter = CoinsListAdapter(requireContext())
        binding.rvCoins.adapter = adapter

        if (isOnline(requireContext())) {
            lifecycleScope.launch {
                when (viewModel.getSortingFromDataStore()) {
                    QUERY_SORT_BY_MARKET_CAP -> {
                        viewModel.setEvent(MainScreenContract.Event.ChoseSortingByMarketCap)
                        updateUi(adapter)
                    }
                    QUERY_SORT_BY_PRICE -> {
                        viewModel.setEvent(MainScreenContract.Event.ChoseSortingByPrice)
                        updateUi(adapter)
                    }
                    QUERY_SORT_BY_VOLATILITY -> {
                        viewModel.setEvent(MainScreenContract.Event.ChoseSortingByVolatility)
                        updateUi(adapter)
                    }
                }
            }
            binding.sortButton.setOnClickListener {
                lifecycleScope.launch {
                    showSortDialog(viewModel.getSortingFromDataStore(), adapter)
                }
            }
            pullToRefresh(adapter)
        } else {
            viewModel.setEvent(MainScreenContract.Event.FetchFromDb)
            updateUi(adapter)
            binding.sortButton.visibility = View.GONE
        }

        //Show bottom nav menu
        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_menu)
        bottomMenu.isVisible = true
    }

    private fun pullToRefresh(adapter: CoinsListAdapter) {
        binding.swipeToRefreshLayout.setOnRefreshListener {
            when (viewModel.currentState.recycleViewState) {
                is MainScreenContract.RecycleViewState.SortingByMarketCap -> {
                    viewModel.setEvent(MainScreenContract.Event.ChoseSortingByMarketCap)
                    updateUi(adapter)
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                is MainScreenContract.RecycleViewState.SortingByPrice -> {
                    viewModel.setEvent(MainScreenContract.Event.ChoseSortingByPrice)
                    updateUi(adapter)
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                is MainScreenContract.RecycleViewState.SortingByVolatility -> {
                    viewModel.setEvent(MainScreenContract.Event.ChoseSortingByVolatility)
                    updateUi(adapter)
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                is MainScreenContract.RecycleViewState.Loading -> {
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun updateUi(adapter: CoinsListAdapter) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it.recycleViewState) {
                        is MainScreenContract.RecycleViewState.Loading -> {
                            binding.apply {
                                mainScreenProgressbar.isVisible = true
                                rvCoins.isVisible = false
                            }
                        }
                        is MainScreenContract.RecycleViewState.SortingByMarketCap -> {
                            viewModel.saveSortingIntoDataStore(QUERY_SORT_BY_MARKET_CAP)
                            it.recycleViewState.coins.distinctUntilChanged()
                                .collectLatest { coins ->
                                    adapter.submitData(coins)
                                }
                        }
                        is MainScreenContract.RecycleViewState.SortingByPrice -> {
                            viewModel.saveSortingIntoDataStore(QUERY_SORT_BY_PRICE)
                            it.recycleViewState.coins.distinctUntilChanged()
                                .collectLatest { coins ->
                                    adapter.submitData(coins)
                                }
                        }
                        is MainScreenContract.RecycleViewState.SortingByVolatility -> {
                            viewModel.saveSortingIntoDataStore(QUERY_SORT_BY_VOLATILITY)
                            it.recycleViewState.coins.distinctUntilChanged()
                                .collectLatest { coins ->
                                    adapter.submitData(coins)
                                }
                        }
                        is MainScreenContract.RecycleViewState.ItemsFromDb -> {
                            it.recycleViewState.coins.distinctUntilChanged()
                                .collectLatest { coins ->
                                    adapter.submitData(coins)
                                }
                        }
                    }
                }
            }
        }
    }

    private fun showSortDialog(order: String, adapter: CoinsListAdapter) {
        val options = arrayOf(
            getString(R.string.dialog_sorted_by_cap),
            getString(R.string.dialog_sorted_by_price),
            getString(R.string.dialog_sorted_by_vol)
        )
        var checkedItem = 0
        when (order) {
            QUERY_SORT_BY_MARKET_CAP -> checkedItem = 0
            QUERY_SORT_BY_PRICE -> checkedItem = 1
            QUERY_SORT_BY_VOLATILITY -> checkedItem = 2
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.sort_dialog_title))
            .setSingleChoiceItems(options, checkedItem, null)
            .setPositiveButton(getString(R.string.positive_button)) { dialog, _ ->
                when ((dialog as AlertDialog).listView.checkedItemPosition) {
                    0 -> {
                        viewModel.setEvent(MainScreenContract.Event.ChoseSortingByMarketCap)
                        updateUi(adapter)
                        binding.rvCoins.layoutManager?.scrollToPosition(0)
                    }
                    1 -> {
                        viewModel.setEvent(MainScreenContract.Event.ChoseSortingByPrice)
                        updateUi(adapter)
                        binding.rvCoins.layoutManager?.scrollToPosition(0)
                    }
                    2 -> {
                        viewModel.setEvent(MainScreenContract.Event.ChoseSortingByVolatility)
                        updateUi(adapter)
                        binding.rvCoins.layoutManager?.scrollToPosition(0)
                    }
                }
            }
            .setNegativeButton(getString(R.string.negative_button)) { _, _ -> }
            .create()
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}