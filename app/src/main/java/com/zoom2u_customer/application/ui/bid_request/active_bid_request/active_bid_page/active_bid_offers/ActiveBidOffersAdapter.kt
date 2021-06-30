package com.zoom2u_customer.application.ui.bid_request.active_bid_request.active_bid_page.active_bid_offers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


import com.zoom2u_customer.databinding.ItemActiveBidOffersBinding


class ActiveBidOffersAdapter (val context: Context, private val dataList: List<ActiveBidOffers>) :
    RecyclerView.Adapter<ActiveBidOffersAdapter.BindingViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemActiveBidOffersBinding =
            ItemActiveBidOffersBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val activeBidOffers: ActiveBidOffers = dataList[position]
        holder.itemBinding.activebidoffers= activeBidOffers


    }


    class BindingViewHolder(val itemBinding: ItemActiveBidOffersBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}