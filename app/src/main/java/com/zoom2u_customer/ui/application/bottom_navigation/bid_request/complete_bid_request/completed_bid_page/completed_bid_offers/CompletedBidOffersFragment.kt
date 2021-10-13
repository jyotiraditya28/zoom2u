package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.completed_bid_offers

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import com.zoom2u_customer.databinding.FragmentBidOffersBinding
import com.zoom2u_customer.databinding.FragmentCompletedBidOffersBinding
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.CompletedDetailsResponse
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.Offer
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.completed_bid_details.CompletedDetailsFragment

class CompletedBidOffersFragment() : Fragment() {
    lateinit var binding: FragmentCompletedBidOffersBinding

    var bidDetail: CompletedDetailsResponse? = null

    companion object {

        var bidDetail1: CompletedDetailsResponse? = null

        fun newInstance(bidDetails: CompletedDetailsResponse?): CompletedBidOffersFragment {
            this.bidDetail1 = bidDetails
            return CompletedBidOffersFragment()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompletedBidOffersBinding.inflate(inflater, container, false)
        this.bidDetail= bidDetail1
        if (container != null) {
            setAdapterView(binding, container.context)
        }

        return binding.root
    }

    fun setAdapterView(binding: FragmentCompletedBidOffersBinding, context: Context) {
        val layoutManager = GridLayoutManager(activity, 1)
        binding.activeBidOffersRecycler.layoutManager = layoutManager
        val adapter =
            CompletedBidOffersAdapter(context, bidDetail?.Offers?.toList()!!, onItemClick = ::onBidOfferSelected)
        binding.activeBidOffersRecycler.adapter = adapter

    }
    private fun onBidOfferSelected(offer:Offer) {

    }
}