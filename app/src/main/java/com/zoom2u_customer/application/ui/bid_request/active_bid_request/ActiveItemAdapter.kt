package com.zoom2u_customer.application.ui.bid_request.active_bid_request

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.databinding.ItemActiveBidBinding


class ActiveItemAdapter (val context: Context, private val dataList: List<ActiveBidItem>, private val onItemClick:(ActiveBidItem) -> Unit) :
    RecyclerView.Adapter<ActiveItemAdapter.BindingViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemActiveBidBinding =
            ItemActiveBidBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        if (position == itemCount - 1)
            holder.itemBinding.blankView.setVisibility(View.VISIBLE)
        else
            holder.itemBinding.blankView.setVisibility(View.GONE)
        val activeBidItem: ActiveBidItem = dataList[position]
        holder.itemBinding.activebid= activeBidItem
        holder.itemBinding.root.setOnClickListener {
            onItemClick(activeBidItem)
        }

    }


    class BindingViewHolder(val itemBinding: ItemActiveBidBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}