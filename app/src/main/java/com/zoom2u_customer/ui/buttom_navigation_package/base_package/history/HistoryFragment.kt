package com.zoom2u_customer.ui.buttom_navigation_package.base_package.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.zoom2u_customer.apiclient.ApiClient.Companion.getServices
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.history.history_details.HistoryDetailsActivity
import com.zoom2u_customer.databinding.FragmentHistoryBinding
import com.zoom2u_customer.utility.AppUtility

import java.util.*

class HistoryFragment : Fragment() {

    lateinit var viewModel: HistoryViewModel
    private var repository: HistoryRepository? = null
    lateinit var binding: FragmentHistoryBinding
    private var adapter: HistoryItemAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        if (container != null) {
            setAdapterView(binding, container.context)
        }

        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        val serviceApi: ServiceApi = getServices()
        repository = HistoryRepository(serviceApi, container?.context)
        viewModel.repository = repository
        viewModel.getHistory()


        viewModel.getHistoryList()?.observe(viewLifecycleOwner) {
            if (it != null) {
                AppUtility.progressBarDissMiss()
                if (it.isNotEmpty()) {

                    adapter?.updateRecords(it)
                    binding.noHistoryText.visibility = View.GONE
                } else

                    binding.noHistoryText.visibility = View.VISIBLE
            }

        }
        return binding.root

    }

    fun setAdapterView(binding: FragmentHistoryBinding, context: Context) {
        val layoutManager = GridLayoutManager(activity, 1)
        binding.deliveryHistoryRecycler.layoutManager = layoutManager
        adapter = HistoryItemAdapter(context, Collections.emptyList(), onItemClick = ::onItemClick)
        binding.deliveryHistoryRecycler.adapter = adapter

    }

    private fun onItemClick(historyResponse: HistoryResponse) {
        val intent = Intent(activity, HistoryDetailsActivity::class.java)
        intent.putExtra("HistoryItem", historyResponse)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }
}