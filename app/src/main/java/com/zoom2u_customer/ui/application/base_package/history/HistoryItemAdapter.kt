package com.zoom2u_customer.ui.application.base_package.history

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.databinding.ItemDeliveryHistoryBinding
import java.util.*

class HistoryItemAdapter(val context: Context, private var dataList: List<HistoryResponse>, private val onItemClick:(HistoryResponse) -> Unit) :
    RecyclerView.Adapter<HistoryItemAdapter.BindingViewHolder>() {
    private var ongoingList:MutableList<HistoryResponse> = ArrayList()
    private var pastList:MutableList<HistoryResponse> = ArrayList()

    fun updateRecords(dataList: List<HistoryResponse>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemDeliveryHistoryBinding =
            ItemDeliveryHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

       /*space in below last item */
        if (position == itemCount - 1)
            holder.itemBinding.blankView.visibility = View.VISIBLE
        else
            holder.itemBinding.blankView.visibility = View.GONE

        val historyResponse: HistoryResponse = dataList[position]
        holder.itemBinding.historyitem= historyResponse
        holder.itemBinding.root.setOnClickListener {
            onItemClick(historyResponse)
        }
        setStatus(dataList[position].Status,holder)



      /*  val createdDateTime = AppUtility.getDateTime(dataList[position].DropDateTime)
        if (System.currentTimeMillis() < createdDateTime.time) {
           ongoingList.add(dataList[position])
        }else
           pastList.add(dataList[position])





        if(ongoingList.size>0){
            if(position==0){
                holder.itemBinding.heading.visibility= View.VISIBLE
                holder.itemBinding.heading.text = "Ongoing(1)"
            if(position==ongoingList.size+1){
                holder.itemBinding.heading.visibility= View.VISIBLE
                holder.itemBinding.heading.text = "Past"
            }
        }
        }*/


        when (position) {
            0 -> {
                holder.itemBinding.heading.visibility= View.VISIBLE
                holder.itemBinding.heading.text = "Ongoing(1)"
            }
            1 -> {
                holder.itemBinding.heading.visibility= View.VISIBLE
                holder.itemBinding.heading.text = "Past"
            }
            else -> holder.itemBinding.heading.visibility= View.GONE
        }

    }

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
    class BindingViewHolder(val itemBinding: ItemDeliveryHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}