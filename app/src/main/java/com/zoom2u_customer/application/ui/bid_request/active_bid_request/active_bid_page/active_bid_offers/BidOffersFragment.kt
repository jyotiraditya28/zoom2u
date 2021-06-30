package com.zoom2u_customer.application.ui.bid_request.active_bid_request.active_bid_page.active_bid_offers

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import com.zoom2u_customer.databinding.FragmentBidOffersBinding


class BidOffersFragment : Fragment() {
    lateinit var binding: FragmentBidOffersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBidOffersBinding.inflate(inflater, container, false)

        if (container != null) {
            setAdpterView(binding, container.context)
        }

        return binding.root
    }

    fun setAdpterView(binding: FragmentBidOffersBinding, context: Context) {
        var layoutManager = GridLayoutManager(activity, 1)
        binding.activeBidOffersRecycler.layoutManager = layoutManager
        var adapter =
            ActiveBidOffersAdapter(context, ActiveBidOffersProvider.activebidOffers.toList())
        binding.activeBidOffersRecycler.adapter = adapter

    }

}