package com.zoom2u_customer.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.BidImagesShowBinding
import com.zoom2u_customer.databinding.ItemDocShowBinding
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.ShowBidImageAdapter
import com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.model.ShipmentsClass
import com.zoom2u_customer.ui.application.bottom_navigation.home.home_fragment.Icon

class DocItemShowAdapter(val context : Context?,
                         private val dataList: MutableList<ShipmentsClass>,
                         ) : RecyclerView.Adapter<DocItemShowAdapter.BindingViewHolder>() {


    override fun getItemCount(): Int {
        return dataList.size
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
             holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_documents)
            }
            "Bag"
            -> {
                holder.itemBinding.docTxt.text="Satchel,laptops"
                holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_satchel_and_laptops)
            }
            "Box" -> {
                holder.itemBinding.docTxt.text="Small box"
                holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_small_box)
            }
            "Flowers" -> {
                holder.itemBinding.docTxt.text="Cakes, flowers,delicates"
                holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_cakes_flowers_delicates)
            }
            "Large" -> {
                holder.itemBinding.docTxt.text="Large box"
                holder.itemBinding.icon.setBackgroundResource(R.drawable.ic_large_box)
            }

        }
        holder.itemBinding.count.text= "${dataList[position].Quantity.toString()}x"



    }


    class BindingViewHolder(val itemBinding: ItemDocShowBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}