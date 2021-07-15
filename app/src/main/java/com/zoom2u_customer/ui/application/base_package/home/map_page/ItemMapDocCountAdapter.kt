package com.zoom2u_customer.ui.application.base_package.home.map_page

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.ui.application.base_package.home.home_fragment.Icon
import com.zoom2u_customer.databinding.ItemMapDocBinding

class ItemMapDocCountAdapter(
    val context: Context,
    private val dataList: List<Icon>,
    private val onItemClick: (Icon) -> Unit
) : RecyclerView.Adapter<ItemMapDocCountAdapter.BindingViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ItemMapDocBinding =
            ItemMapDocBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val icon: Icon = dataList[position]
        holder.itemBinding.icon = icon
        if (icon.quantity > 0)
            holder.itemBinding.doc.visibility = View.VISIBLE
        else
            holder.itemBinding.doc.visibility = View.GONE


        holder.itemBinding.root.setOnClickListener {
            onItemClick(icon)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateItem(icon: Icon?) {
        dataList.forEachIndexed { index, pod ->
            if (pod.text == icon?.text) {
                pod.apply {
                    quantity = icon.quantity
                    weight = icon.weight
                    height = icon.height
                    length = icon.length
                    width = icon.width
                }
                notifyItemChanged(index)

            }
        }
    }


    class BindingViewHolder(val itemBinding: ItemMapDocBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}