package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.CompletedBidActivity
import com.zoom2u_customer.databinding.FragmentActiveBidBinding
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.ActiveBidActivity
import com.zoom2u_customer.utility.AppUtility


class ActiveBidFragment : Fragment() {
    lateinit var binding: FragmentActiveBidBinding
    lateinit var viewModel: ActiveBidListViewModel
    private var  repository: ActiveBidListRepository? = null
    private var adapter: ActiveItemAdapter? = null
    private var currentPage: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentActiveBidBinding.inflate(inflater, container, false)

        if (container != null) {
            setAdapterView(binding,container.context)
        }
        viewModel = ViewModelProvider(this).get(ActiveBidListViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = ActiveBidListRepository(serviceApi, container?.context)
        viewModel.repository = repository
        currentPage = 1
        viewModel.getActiveBidList(currentPage)

        binding.swipeRefresh.setOnRefreshListener(OnRefreshListener {
            viewModel.getActiveBidList(1)
        })

        viewModel.getActiveBidListSuccess()?.observe(viewLifecycleOwner) {
            if (it != null) {

                /**for first release hide freight and xl item  from list*/
                val listWithOutFreight: MutableList<ActiveBidListResponse> = ArrayList()
                AppUtility.progressBarDissMiss()
                binding.swipeRefresh.isRefreshing = false
                if (it.isNotEmpty()) {
                    AppUtility.progressBarDissMiss()
                    for(item in it){
                        if(item.ItemType=="Freight"||item.ItemCategory=="XL")
                            continue
                        else
                            listWithOutFreight.add(item)
                    }

                    adapter?.updateRecords(listWithOutFreight)
                    binding.noActiveBidText.visibility = View.GONE
                }else{
                    binding.noActiveBidText.visibility = View.VISIBLE
                }
            }
        }


        return binding.root

    }
    private fun setAdapterView(binding: FragmentActiveBidBinding, context: Context) {
        val layoutManager = GridLayoutManager(activity, 1)
        binding.activeBidRecycler.layoutManager = layoutManager
        adapter = ActiveItemAdapter(context,
            onItemClick = ::onItemClick,
            onApiCall = ::onApiCall)
        binding.activeBidRecycler.adapter=adapter

    }

    private fun onApiCall() {
        currentPage++
        AppUtility.progressBarShow(activity)
        viewModel.getActiveBidList(currentPage)
    }
    private fun onItemClick(activeBidItem: ActiveBidListResponse){
        val intent = Intent(activity, ActiveBidActivity::class.java)
        intent.putExtra("QuoteId",activeBidItem.Id.toString())
        startActivity(intent)
    }

}