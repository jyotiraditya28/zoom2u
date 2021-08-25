package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.ServiceApi
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.zoom2u_customer.databinding.FragmentCompleteBidBinding
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.ActiveBidListResponse
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.CompletedBidActivity
import com.zoom2u_customer.utility.AppUtility

class CompleteBidFragment : Fragment() {
    lateinit var binding: FragmentCompleteBidBinding
    lateinit var viewModel: CompletedBidListViewModel
    private var  repository: CompletedBidListRepository? = null
    private var adapter:CompletedItemAdapter? = null
    private var currentPage: Int = 1
    val listData: MutableList<CompletedBidListResponse> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompleteBidBinding.inflate(inflater, container, false)

        if (container != null) {
            setAdapterView(binding,container.context)
        }
        viewModel = ViewModelProvider(this).get(CompletedBidListViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = CompletedBidListRepository(serviceApi, container?.context)
        viewModel.repository = repository
        currentPage = 1
        viewModel.getCompletedBidList(currentPage)

        binding.swipeRefresh.setOnRefreshListener(OnRefreshListener {
           listData.clear()
            viewModel.getCompletedBidList(1)
        })

        viewModel.getCompletedBidListSuccess()?.observe(viewLifecycleOwner) {
            if (it != null) {

                AppUtility.progressBarDissMiss()
                binding.swipeRefresh.isRefreshing = false
                if (it.isNotEmpty()) {
                    updateRecord(it.toMutableList())
                    binding.noCompletedBidText.visibility = View.GONE
                }else{

                    binding.noCompletedBidText.visibility = View.VISIBLE
                }
            }
        }


        return binding.root

    }

    private fun updateRecord(listWithOutFreight: MutableList<CompletedBidListResponse>) {
        if (listWithOutFreight.size > 0)
            listData.addAll(listWithOutFreight)
        else {
            listData.clear()
            listData.addAll(listWithOutFreight)
        }

        adapter?.updateRecords(listData)
    }



    private fun setAdapterView(binding: FragmentCompleteBidBinding, context: Context) {
        val layoutManager = GridLayoutManager(activity, 1)
        binding.completedBidRecycler.layoutManager = layoutManager
        adapter = CompletedItemAdapter(context,
            onItemClick = ::onItemClick,
            onApiCall = ::onApiCall)
        binding.completedBidRecycler.adapter=adapter

    }

    private fun onApiCall() {
        currentPage++
        AppUtility.progressBarShow(activity)
        viewModel.getCompletedBidList(currentPage)
    }
    private fun onItemClick(completedBidItem: CompletedBidListResponse){
        val intent = Intent(activity, CompletedBidActivity::class.java)
        intent.putExtra("QuoteId",completedBidItem.Id.toString())
        startActivity(intent)
    }

}