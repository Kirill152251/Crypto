package com.example.crypto.views.fragments.mainScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crypto.R
import com.example.crypto.databinding.FragmentMainScreenBinding
import com.example.crypto.model.constans.SORT_BY_MARKET_CAP
import com.example.crypto.model.constans.SORT_BY_PRICE
import com.example.crypto.model.constans.SORT_BY_VOLATILITY
import com.example.crypto.viewModels.CoinsListViewModel
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CoinsListViewModel by viewModel()

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

        binding.rvCoins.addItemDecoration(decoration)
        binding.rvCoins.adapter = adapter

        //by default list sorted by market capitalization
        submitDataIntoAdapter(SORT_BY_MARKET_CAP, adapter)

        binding.mainScreenToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.sort_by_price -> {
                    submitDataIntoAdapter(SORT_BY_PRICE, adapter)
                    //scrollToTheTop(layoutManager)
                    //binding.rvCoins.smoothScrollToPosition(0)
                    Toast.makeText(requireContext(), resources.getString(R.string.sorted_by_price), Toast.LENGTH_LONG).show()
                    true
                }
                R.id.sort_by_market_cap -> {
                    submitDataIntoAdapter(SORT_BY_MARKET_CAP, adapter)
                    //scrollToTheTop(layoutManager)
                    //binding.rvCoins.smoothScrollToPosition(0)
                    Toast.makeText(requireContext(), resources.getString(R.string.sorted_by_market_cap), Toast.LENGTH_LONG).show()
                    true
                }
                R.id.sort_by_volatility -> {
                    submitDataIntoAdapter(SORT_BY_VOLATILITY, adapter)
                    //scrollToTheTop(layoutManager)
                    //binding.rvCoins.smoothScrollToPosition(0)
                    Toast.makeText(requireContext(), resources.getString(R.string.sorted_by_volatility), Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun submitDataIntoAdapter(order: String, adapter: CoinsListAdapter) {
        lifecycleScope.launch {
            //binding.rvCoins.layoutManager?.scrollToPosition(0)
            viewModel.getCoins(order).distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }
    }

//    private fun scrollToTheTop(layoutManager: LinearLayoutManager) {
//        if (layoutManager.findLastVisibleItemPosition() != 0) {
//            binding.rvCoins.smoothScrollToPosition(0)
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}