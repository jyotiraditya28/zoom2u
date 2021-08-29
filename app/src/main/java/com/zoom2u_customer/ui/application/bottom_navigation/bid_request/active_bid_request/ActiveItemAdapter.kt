package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ItemActiveBidBinding
import com.zoom2u_customer.ui.application.bottom_navigation.history.HistoryItemAdapter
import com.zoom2u_customer.ui.application.bottom_navigation.history.HistoryResponse
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import java.util.ArrayList


class ActiveItemAdapter (val context: Context, private val onItemClick:(ActiveBidListResponse) -> Unit, private val onApiCall:() ->Unit,
                         private val onCancelClick:(Int,String) -> Unit) :
    RecyclerView.Adapter<ActiveItemAdapter.BindingViewHolder>() {
    private var dataList:MutableList<ActiveBidListResponse> = ArrayList()
    private var lastApiCallPosition:Int=-1

    override fun getItemCount(): Int {
        return dataList.size
    }
    fun updateRecords(dataList1: List<ActiveBidListResponse>) {
        dataList.clear()
        this.dataList.addAll(dataList1)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemActiveBidBinding =
            ItemActiveBidBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        if (position == itemCount - 1)
            holder.itemBinding.blankView.visibility = View.VISIBLE
        else
            holder.itemBinding.blankView.visibility = View.GONE
        val activeBidItem: ActiveBidListResponse = dataList[position]
        holder.itemBinding.activebid= activeBidItem

        holder.itemBinding.root.setOnClickListener {
           if(dataList[position].ItemType=="Freight"||dataList[position].ItemCategory=="XL"){
               DialogActivity.alertDialogSingleButton(context, "Alert!", "Currently not showing Freight and Extra Large item information try in portal.")
           }else
               onItemClick(activeBidItem)
        }

    /**for pagination*/
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

        if(dataList[position].Notes.isNullOrEmpty()){
            holder.itemBinding.notes.text="No notes available"
        }else{
            holder.itemBinding.notes.text=dataList[position].Notes.toString().trim()
        }


        holder.itemBinding.ref.text= "Ref #:${dataList[position].QuoteRef.toString()}"

        holder.itemBinding.cancel.setOnClickListener{
            onCancelClick(dataList[position].Id!!,dataList[position].ItemType!!)
        }

        setItemType(holder,dataList[position])
    }


    fun setItemType(holder: BindingViewHolder,data:ActiveBidListResponse){
        if(data.ItemType=="ExtraLargeItem") {
            when (data.ItemCategory) {
                "Documents" -> {
                    holder.itemBinding.docTxt.text = "Documents"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_documents_low)
                }
                "Bag"
                -> {
                    holder.itemBinding.docTxt.text = "Satchel,laptops"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_satchelandlaptops_low)
                }
                "Box" -> {
                    holder.itemBinding.docTxt.text = "Small box"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_smallbox_low)
                }
                "Flowers" -> {
                    holder.itemBinding.docTxt.text = "Cakes, flowers,delicates"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_cakesflowersdelicates_low)
                }
                "Large" -> {
                    holder.itemBinding.docTxt.text = "Large box"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_largebox_low)
                }
                "XL" ->{
                    holder.itemBinding.docTxt.text = "Large items"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_machinery)
                }
            }
        }
        if(data.ItemType=="Freight") {
            when (data.ItemCategory) {
              "0" ->{
                  holder.itemBinding.docTxt.text = "General Van Shipments"
                  holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_general_van_shipments)
              }



                "2" -> {
                    holder.itemBinding.docTxt.text = "Building Materials"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_building_materials)
                }
                "3"
                -> {
                    holder.itemBinding.docTxt.text = "General Truck Shipments"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_general_truck_shipments)
                }
                "4" -> {
                    holder.itemBinding.docTxt.text = "Pallets"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_pallets)
                }
                "5" -> {
                    holder.itemBinding.docTxt.text = "Marchinery"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_machinery)
                }
                "6" -> {
                    holder.itemBinding.docTxt.text = "Vehicles"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_vehicles)
                }
                "7" ->{
                    holder.itemBinding.docTxt.text = "Container"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_container)
                }
                "8" ->{
                    holder.itemBinding.docTxt.text = "Full Truck Load"
                    holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_full_truck_load)
                }
                "9" ->{

                }
                "10" ->{

                }
            }
        }


    }


    class BindingViewHolder(val itemBinding: ItemActiveBidBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}