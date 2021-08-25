package com.zoom2u_customer.ui.application.bottom_navigation.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.zoom2u_customer.apiclient.ApiClient.Companion.getServices
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.FragmentHistoryBinding
import com.zoom2u_customer.ui.application.bottom_navigation.history.history_details.HistoryDetailsActivity
import com.zoom2u_customer.utility.AppUtility
import kotlin.collections.ArrayList


class HistoryFragment : Fragment() {
    lateinit var viewModel: HistoryViewModel
    private var repository: HistoryRepository? = null
    lateinit var binding: FragmentHistoryBinding
    private var adapter: HistoryItemAdapter? = null
    private var currentPage: Int = 1
    private var mergeHistoryList: MutableList<HistoryResponse> = ArrayList()

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
        currentPage = 1
        viewModel.getHistory(currentPage)
        if(mergeHistoryList.size>0)
        mergeHistoryList.clear()

        viewModel.getHistoryList()?.observe(viewLifecycleOwner) {
            if (it != null) {
                AppUtility.progressBarDissMiss()
                binding.swipeRefresh.isRefreshing = false
                if (it.isNotEmpty()) {
                    AppUtility.progressBarDissMiss()

                    val onGoingList: MutableList<HistoryResponse> = ArrayList()
                    val pastList: MutableList<HistoryResponse> = ArrayList()

                    /**count ongoing*/
                    for (item in it) {
                        if (System.currentTimeMillis() < AppUtility.getDateTime(item.DropDateTime).time) {
                            onGoingList.add(item)
                        } else {
                            pastList.add(item)
                        }
                    }
                    if(mergeHistoryList.size>0) {
                        /**when pagination work*/
                        mergeHistoryList.addAll(onGoingList)
                        mergeHistoryList.addAll(pastList)

                    }else{
                        /**when first time and swipe refresh activity lunch*/
                        mergeHistoryList.clear()
                        mergeHistoryList.add(HistoryResponse().apply {
                            count = onGoingList.size
                            type = 1
                        })
                        mergeHistoryList.addAll(onGoingList)

                        mergeHistoryList.add(HistoryResponse().apply {
                            count=pastList.size
                            type=2
                        })
                        mergeHistoryList.addAll(pastList)
                    }
                    adapter?.updateRecords(mergeHistoryList)
                    binding.noHistoryText.visibility = View.GONE
                } else
                    binding.noHistoryText.visibility = View.VISIBLE
            }

        }




        binding.swipeRefresh.setOnRefreshListener(OnRefreshListener {
            mergeHistoryList.clear()
            viewModel.getHistory(1)
        })



        return binding.root

    }

    fun setAdapterView(binding: FragmentHistoryBinding, context: Context) {
        val layoutManager = GridLayoutManager(activity, 1)
        binding.deliveryHistoryRecycler.layoutManager = layoutManager
        adapter = HistoryItemAdapter(
            context,
            onItemClick = ::onItemClick,
            onApiCall = ::onApiCall
        )
        binding.deliveryHistoryRecycler.adapter = adapter

    }

    private fun onApiCall() {
        currentPage++
        AppUtility.progressBarShow(activity)
        viewModel.getHistory(currentPage)
    }

    private fun onItemClick(historyResponse: HistoryResponse) {
        val intent = Intent(activity, HistoryDetailsActivity::class.java)
        intent.putExtra("HistoryItem", historyResponse)
        intent.putParcelableArrayListExtra("mergeHistoryList", ArrayList(mergeHistoryList.toList()))
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivityForResult(intent,3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3 ) {
            val updatedHistoryItem: HistoryResponse? = data?.getParcelableExtra<HistoryResponse>("historyItem")
            adapter?.updateItem(updatedHistoryItem)
        }
    }
}

