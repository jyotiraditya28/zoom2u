package com.zoom2u_customer.ui.application.bottom_navigation.history.history_details

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ItemDocShowBinding
import com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.model.ShipmentsClass

class DocItemListShowAdapter (val context : Context?,
                              private val dataList: MutableList<ShipmentsClass>,
) : RecyclerView.Adapter<DocItemListShowAdapter.BindingViewHolder>() {

    private var isMoreEnable:Boolean?=null
    override fun getItemCount(): Int {
        return dataList.size

    }

    fun isMoreEnable(isMoreEnable:Boolean){
        this.isMoreEnable=isMoreEnable
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemDocShowBinding =
            ItemDocShowBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {


        when (dataList[position].Category) {
            "Documents" -> {
                holder.itemBinding.docTxt.text="Documents"
                holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_documents_low)
            }
            "Bag"
            -> {
                holder.itemBinding.docTxt.text="Satchel,laptops"
                holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_satchelandlaptops_low)
            }
            "Box" -> {
                holder.itemBinding.docTxt.text="Small box"
                holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_smallbox_low)
            }
            "Flowers" -> {
                holder.itemBinding.docTxt.text="Cakes, flowers,delicates"
                holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_cakesflowersdelicates_low)
            }
            "Large" -> {
                holder.itemBinding.docTxt.text="Large box"
                holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_largebox_low)
            }

        }
        holder.itemBinding.count.text= "${dataList[position].Quantity.toString()}x"



    }


    class BindingViewHolder(val itemBinding: ItemDocShowBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}