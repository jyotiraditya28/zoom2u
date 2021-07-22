package com.zoom2u_customer.ui.application.bottom_navigation.home.home_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.IconItemBinding


class IconAdapter(private val context: Context, private var dataList: MutableList<Icon>) :
    RecyclerView.Adapter<IconAdapter.BindingViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateItem(icon: Icon?) {
        dataList.forEachIndexed { index, pod ->
            if (pod.text == icon?.text) {
                notifyItemChanged(index)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: IconItemBinding =
            IconItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val icon: Icon = dataList[position]
        holder.itemBinding.icon = icon
        if(icon.quantity==0)
            unhighlightView(holder)
        else
            highlightView(holder)


        holder.itemBinding.layoutDocLl.setOnClickListener {
           if(dataList[position].quantity==0){
               dataList[position].quantity+=1
               notifyItemChanged(position)
           }else if(dataList[position].quantity==1){
               dataList[position].quantity-=1
               notifyItemChanged(position)
           }

        }


       if(icon.text == "Large items"){
            //holder.itemBinding.loadMoreIcon.visibility = View.VISIBLE
            holder.itemBinding.layoutDocLl.visibility=View.GONE
           holder.itemBinding.plusMinus.visibility=View.GONE
           holder.itemBinding.text.visibility=View.GONE
        }else{
           holder.itemBinding.layoutDocLl.visibility=View.VISIBLE
           holder.itemBinding.plusMinus.visibility=View.VISIBLE
           holder.itemBinding.text.visibility=View.VISIBLE


        }
        holder.itemBinding.increment.setOnClickListener{
            dataList[position].quantity+=1
            notifyItemChanged(position)
        }
        holder.itemBinding.decrement.setOnClickListener{
            if(dataList[position].quantity>0)
            dataList[position].quantity-=1
            notifyItemChanged(position)
        }



    }

private fun highlightView(holder: BindingViewHolder) {
    holder.itemBinding.layoutDocLl.setBackgroundResource(R.drawable.image_background_onchnag)
}

private fun unhighlightView(holder: BindingViewHolder) {
    holder.itemBinding.layoutDocLl.setBackgroundResource(R.drawable.white_background)
}

class BindingViewHolder(val itemBinding: IconItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root)

    }