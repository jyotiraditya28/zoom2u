package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.databinding.ItemActiveBidBinding
import com.zoom2u_customer.databinding.ItemCompletedBidBinding
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.ActiveBidListResponse
import com.zoom2u_customer.ui.application.bottom_navigation.history.HistoryResponse
import com.zoom2u_customer.utility.AppUtility
import java.util.ArrayList


class CompletedItemAdapter (val context: Context, private val onItemClick:(CompletedBidListResponse) -> Unit, private val onApiCall:() ->Unit) :
    RecyclerView.Adapter<CompletedItemAdapter.BindingViewHolder>() {
    private var dataList:MutableList<CompletedBidListResponse> = ArrayList()
    private var lastApiCallPosition:Int=-1
    override fun getItemCount(): Int {
        return dataList.size
    }
    fun updateRecords(dataList1: List<CompletedBidListResponse>) {
       if(!dataList.isNullOrEmpty()){
           if(dataList1[0].Id==dataList[0].Id){
               dataList.clear()
               this.dataList.addAll(dataList1)
           }else{
               this.dataList.addAll(dataList1)
           }


       }else{
           this.dataList.addAll(dataList1)
       }
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemCompletedBidBinding =
            ItemCompletedBidBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        if (position == itemCount - 1)
            holder.itemBinding.blankView.visibility = View.VISIBLE
        else
            holder.itemBinding.blankView.visibility = View.GONE
        val completedBidItem: CompletedBidListResponse = dataList[position]
        holder.itemBinding.completedbid= completedBidItem
        holder.itemBinding.root.setOnClickListener {
            onItemClick(completedBidItem)
        }
        if(position==dataList.size-1) {
            if(lastApiCallPosition!=position){
                lastApiCallPosition=position
                onApiCall()
            }
        }


        val pickDateTime = AppUtility.getDateTimeFromDeviceToServerForDate(dataList[position].PickupDateTime)
        val pickUpDateTimeSplit: Array<String>? = pickDateTime?.split(" ")?.toTypedArray()
        holder.itemBinding.pickTime.text =
            pickUpDateTimeSplit?.get(1) + " " + pickUpDateTimeSplit?.get(2) + " | " + pickUpDateTimeSplit?.get(0)

        val dropDateTime = AppUtility.getDateTimeFromDeviceToServerForDate(dataList[position].DropDateTime)
        val dropUpDateTimeSplit: Array<String>? = dropDateTime?.split(" ")?.toTypedArray()
        holder.itemBinding.dropTime.text =
            dropUpDateTimeSplit?.get(1) + " " + dropUpDateTimeSplit?.get(2) + " | " + dropUpDateTimeSplit?.get(0)

    }


    class BindingViewHolder(val itemBinding: ItemCompletedBidBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}