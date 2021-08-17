package com.zoom2u_customer.ui.application.bottom_navigation.history

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.databinding.ItemDeliveryHistoryBinding
import com.zoom2u_customer.utility.AppUtility
import java.text.SimpleDateFormat
import java.util.*

class HistoryItemAdapter(val context: Context, private val onItemClick:(HistoryResponse) -> Unit,private val onApiCall:() ->Unit) :
    RecyclerView.Adapter<HistoryItemAdapter.BindingViewHolder>() {
    private var dataList1:MutableList<HistoryResponse> = ArrayList()
    private var lastApiCallPosition:Int=-1


    fun updateRecords(dataList: MutableList<HistoryResponse>) {
        dataList1.clear()
        this.dataList1.addAll(dataList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList1.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemDeliveryHistoryBinding =
            ItemDeliveryHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        if(dataList1[position].type==1){
            if(dataList1[position].count>0) {
                holder.itemBinding.heading.visibility = View.VISIBLE
                holder.itemBinding.heading.text = "Ongoing(${dataList1[position].count})"
                holder.itemBinding.cl2.visibility = View.GONE
            }else{
                holder.itemBinding.cl2.visibility = View.GONE
            }
        }else if(dataList1[position].type==2){
            holder.itemBinding.heading.visibility= View.VISIBLE
            holder.itemBinding.heading.text = "Past"
            holder.itemBinding.cl2.visibility=View.GONE
        }else{
            /*space in below last item */
            if (position == itemCount - 1)
                holder.itemBinding.blankView.visibility = View.VISIBLE
            else
                holder.itemBinding.blankView.visibility = View.GONE

            val historyResponse: HistoryResponse = dataList1[position]
            holder.itemBinding.historyitem= historyResponse
            holder.itemBinding.root.setOnClickListener {
                onItemClick(historyResponse)
            }

            if(position==dataList1.size-1) {
                if(lastApiCallPosition!=position){
                    lastApiCallPosition=position
                    onApiCall()
                }
            }

            if(dataList1[position].IsCancel==true) {
                holder.itemBinding.price.text = "No Charge"
                holder.itemBinding.status.text = "Cancelled"
                holder.itemBinding.status.setBackgroundColor(Color.parseColor("#ff0000"))
                holder.itemBinding.status.setTextColor(Color.WHITE)
            }else {
                holder.itemBinding.price.text = "$" + dataList1[position].Price.toString()
                setStatus(dataList1[position].Status,holder)
            }


        }


    }

    @SuppressLint("SetTextI18n")
    private fun setStatus(status: String?, holder: HistoryItemAdapter.BindingViewHolder) {


        when (status) {
            "Accepted" -> {
                holder.itemBinding.status.text = "Allocated"
                holder.itemBinding.status.setBackgroundColor(Color.parseColor("#00A7E2"))
                holder.itemBinding.status.setTextColor(Color.BLACK)
            }
            "Picked up" -> {
                holder.itemBinding.status.text = "Picked up"
                holder.itemBinding.status.setBackgroundColor(Color.parseColor("#00A7E2"))
                holder.itemBinding.status.setTextColor(Color.WHITE)
            }
            "Dropped Off" -> {
                holder.itemBinding.status.text ="Delivered"
                holder.itemBinding.status.setBackgroundColor(Color.parseColor("#76D750"))
                holder.itemBinding.status.setTextColor(Color.BLACK)
            }
            "On Route to Pickup" -> {
                holder.itemBinding.status.text ="On Route to-Pickup"
                holder.itemBinding.status.setBackgroundColor(Color.parseColor("#00A7E2"))
                holder.itemBinding.status.setTextColor(Color.WHITE)
            }
            "On Route to Dropoff" -> {
                holder.itemBinding.status.text ="On Route to-Dropoff"
                holder.itemBinding.status.setBackgroundColor(Color.parseColor("#00A7E2"))
                holder.itemBinding.status.setTextColor(Color.WHITE)
            }
           else -> {
                holder.itemBinding.status.text ="Accepted"
                holder.itemBinding.status.setBackgroundColor(Color.parseColor("#FFD100"))
                holder.itemBinding.status.setTextColor(Color.BLACK)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateItem(updatedHistoryItem: HistoryResponse?) {
        dataList1.forEachIndexed { index, pod ->
            if (pod.BookingRef == updatedHistoryItem?.BookingRef) {
                pod.apply {
                    IsCancel=updatedHistoryItem?.IsCancel
                }
                notifyItemChanged(index)

            }
        }
    }

    class BindingViewHolder(val itemBinding: ItemDeliveryHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}