package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.active_bid_offers

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import com.zoom2u_customer.databinding.FragmentBidOffersBinding
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.BidDetailsResponse
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.CompletedDetailsResponse
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.Offer
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.completed_bid_offers.CompletedBidOffersAdapter

class BidOffersFragment(private val bidDetails: BidDetailsResponse?) : Fragment() {
    lateinit var binding: FragmentBidOffersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBidOffersBinding.inflate(inflater, container, false)

        if (container != null) {
            setAdapterView(binding, container.context)
        }

        return binding.root
    }

    fun setAdapterView(binding: FragmentBidOffersBinding, context: Context) {
        val layoutManager = GridLayoutManager(activity, 1)
        binding.activeBidOffersRecycler.layoutManager = layoutManager
        val adapter =
            ActiveBidOffersAdapter(context, bidDetails?.Offers?.toList()!!, onItemClick = ::onBidOfferSelected)
        binding.activeBidOffersRecycler.adapter = adapter

    }
    private fun onBidOfferSelected(offer:Offer) {

    }
}