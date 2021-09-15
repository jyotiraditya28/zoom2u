package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.active_bid_offers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.R


import com.zoom2u_customer.databinding.ItemActiveBidOffersBinding
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.Offer
import com.zoom2u_customer.utility.AppUtility


class ActiveBidOffersAdapter(
    val context: Context, private val dataList: List<Offer>,
    private val onItemClick: (Offer) -> Unit
) :
    RecyclerView.Adapter<ActiveBidOffersAdapter.BindingViewHolder>() {
    private var isBidAvailable: Boolean = true
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemActiveBidOffersBinding =
            ItemActiveBidOffersBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val activeBidOffers: Offer = dataList[position]
        holder.itemBinding.activebidoffers = activeBidOffers

        holder.itemBinding.name.text = activeBidOffers.Courier.toString()
        holder.itemBinding.price.text = "$" + activeBidOffers.Price.toString()

        if (!TextUtils.isEmpty(activeBidOffers.CourierImage)) {
            holder.itemBinding.dp.setImageBitmap(AppUtility.getBitmapFromURL(activeBidOffers.CourierImage))
        }

        holder.itemBinding.acceptBid.setOnClickListener {
            if (isBidAvailable)
                onItemClick(dataList[position])
            else
                Toast.makeText(context, "This bid offer is expired.", Toast.LENGTH_SHORT).show()
        }

        /**extra details data*/
        holder.itemBinding.pickupTime.text =
            AppUtility.getDateTimeFromDeviceToServerForDate(dataList[position].PickupETA)
        holder.itemBinding.lastCompletedTime.text =
            AppUtility.getDateTimeFromDeviceToServerForDate(dataList[position].LastCompletedDeliveryDateTime)
        holder.itemBinding.dropTime.text =
            AppUtility.getDateTimeFromDeviceToServerForDate(dataList[position].DropETA)
        holder.itemBinding.registerTime.text =
            AppUtility.getDateTimeFromDeviceToServerForDate(dataList[position].RegisteredWithZoom2uOnDateTime)
        holder.itemBinding.bidHasTime.text =
            AppUtility.getDateTimeFromDeviceToServerForDate(dataList[position].BidActivePeriod)


        /**if BidActivePeriod complete 30min*/
        if (System.currentTimeMillis() > AppUtility.getDateTime(dataList[position].BidActivePeriod).time) {
            isBidAvailable = false
            holder.itemBinding.pickupTime.setTextColor(Color.RED)
            holder.itemBinding.lastCompletedTime.setTextColor(Color.RED)
            holder.itemBinding.dropTime.setTextColor(Color.RED)
            holder.itemBinding.registerTime.setTextColor(Color.RED)
            holder.itemBinding.bidHas.text = "Bid has :"
            holder.itemBinding.bidHasTime.text = "Expired"
            holder.itemBinding.bidHasTime.setTextColor(Color.RED)
            holder.itemBinding.bidExpireNote.visibility = View.VISIBLE
            holder.itemBinding.acceptBid.setBackgroundResource(R.drawable.expire_bid)
            holder.itemBinding.acceptBid.text = "Bid Expired"

        }


        holder.itemBinding.loadMore.setOnClickListener {
            holder.itemBinding.loadMore.visibility = View.GONE
            holder.itemBinding.loadData.visibility = View.VISIBLE
        }
        holder.itemBinding.loadLess.setOnClickListener {
            holder.itemBinding.loadMore.visibility = View.VISIBLE
            holder.itemBinding.loadData.visibility = View.GONE
        }

    }


    class BindingViewHolder(val itemBinding: ItemActiveBidOffersBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}