package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.completed_bid_offers

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


import com.zoom2u_customer.databinding.ItemActiveBidOffersBinding
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.Offer
import com.zoom2u_customer.utility.AppUtility


class CompletedBidOffersAdapter(val context: Context, private val dataList: List<Offer>,
                                private val onItemClick: (Offer) -> Unit) :
    RecyclerView.Adapter<CompletedBidOffersAdapter.BindingViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemActiveBidOffersBinding =
            ItemActiveBidOffersBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val activeBidOffers:Offer = dataList[position]
        holder.itemBinding.activebidoffers= activeBidOffers

        holder.itemBinding.name.text=activeBidOffers.Courier.toString()
        holder.itemBinding.price.text="$"+activeBidOffers.Price.toString()

        if(!TextUtils.isEmpty(activeBidOffers.CustomerImage)) {
            holder.itemBinding.dp.setImageBitmap(AppUtility.getBitmapFromURL(activeBidOffers.CustomerImage))
        }

        holder.itemBinding.acceptBid.setOnClickListener(){
            onItemClick(dataList[position])
        }

    }


    class BindingViewHolder(val itemBinding: ItemActiveBidOffersBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}