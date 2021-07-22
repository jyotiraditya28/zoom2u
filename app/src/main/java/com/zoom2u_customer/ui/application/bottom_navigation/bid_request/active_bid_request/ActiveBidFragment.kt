package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.ActiveBidActivity
import com.zoom2u_customer.databinding.FragmentActiveBidBinding


class ActiveBidFragment : Fragment() {
    lateinit var binding: FragmentActiveBidBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentActiveBidBinding.inflate(inflater, container, false)

        if (container != null) {
            setAdpterView(binding,container.context)
        }

        return binding.root

    }
    fun setAdpterView(binding: FragmentActiveBidBinding,context: Context) {
        var layoutManager = GridLayoutManager(activity, 1)
        binding.activeBidRecycler.layoutManager = layoutManager
        var adapter = ActiveItemAdapter(context, ActiveItemProvider.activebidItem.toList(),onItemClick = ::onItemClick)
        binding.activeBidRecycler.adapter=adapter

    }
    private fun onItemClick(activeBidItem: ActiveBidItem){
        val intent = Intent(activity, ActiveBidActivity::class.java)
        startActivity(intent)
    }

}