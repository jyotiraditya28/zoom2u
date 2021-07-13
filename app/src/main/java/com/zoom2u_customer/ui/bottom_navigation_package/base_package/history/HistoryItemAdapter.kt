package com.zoom2u_customer.ui.bottom_navigation_package.base_package.history

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

class HistoryItemAdapter(val context: Context, private var dataList: List<HistoryResponse>, private val onItemClick:(HistoryResponse) -> Unit) :
    RecyclerView.Adapter<HistoryItemAdapter.BindingViewHolder>() {



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
      var onGoingCount : Int =0
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

        val createdDateTime = AppUtility.getDateTime(dataList[position].CreatedDateTime)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val strDate: Date = sdf.parse(createdDateTime)
        if (System.currentTimeMillis() < strDate.time) {
           onGoingCount++
        }else{

        }





        if(position==0){
            holder.itemBinding.heading.visibility= View.VISIBLE
            holder.itemBinding.heading.text = "Ongoing(1)"
        }else if(position==1){
            holder.itemBinding.heading.visibility= View.VISIBLE
            holder.itemBinding.heading.text = "Past"
        }else
            holder.itemBinding.heading.visibility= View.GONE

    }

    private fun setStatus(status: String?, holder: HistoryItemAdapter.BindingViewHolder) {

        if(status=="Picked up"){
            holder.itemBinding.status.text = "Picked up"
            holder.itemBinding.status.setBackgroundColor(Color.parseColor("#FFD100"))
            holder.itemBinding.status.setTextColor(Color.BLACK)
        }else if(status=="Accepted"){
            holder.itemBinding.status.text = "Accepted"
            holder.itemBinding.status.setBackgroundColor(Color.parseColor("#00A7E2"))
            holder.itemBinding.status.setTextColor(Color.WHITE)
        }else if(status == "Dropped Off"){
            holder.itemBinding.status.text ="Dropped Off"
            holder.itemBinding.status.setBackgroundColor(Color.parseColor("#00A7E2"))
            holder.itemBinding.status.setTextColor(Color.WHITE)
        }else if( status == null){
            holder.itemBinding.status.text ="Allocated"
            holder.itemBinding.status.setBackgroundColor(Color.parseColor("#FF5733"))
            holder.itemBinding.status.setTextColor(Color.WHITE)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    class BindingViewHolder(val itemBinding: ItemDeliveryHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}