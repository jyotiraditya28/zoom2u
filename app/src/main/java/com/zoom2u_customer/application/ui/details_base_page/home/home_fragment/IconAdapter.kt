package com.zoom2u_customer.application.ui.details_base_page.home.home_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.IconItemBinding


class IconAdapter(private val context: Context, private val dataList: List<Icon>) :
    RecyclerView.Adapter<IconAdapter.BindingViewHolder>() {

     var selecteIcon: MutableList<Icon> = ArrayList()





    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: IconItemBinding =
            IconItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val icon: Icon = dataList[position]
        holder.itemBinding.icon = icon
        if(icon.count==0)
            unhighlightView(holder)
        else
            highlightView(holder)


        holder.itemBinding.layoutDocLl.setOnClickListener {

        }



        if(icon.text.equals("Large items")){
            holder.itemBinding.loadMoreIcon.visibility = View.VISIBLE
            holder.itemBinding.plusMinus.visibility = View.GONE
        }else{
            holder.itemBinding.loadMoreIcon.visibility = View.GONE
            holder.itemBinding.plusMinus.visibility = View.VISIBLE

        }




        holder.itemBinding.increment.setOnClickListener{
            dataList[position].count+=1
            notifyItemChanged(position)
        }
        holder.itemBinding.decrement.setOnClickListener{
            if(dataList[position].count>0)
            dataList[position].count-=1
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


fun getSelectedIcon(): MutableList<Icon> {
    return selecteIcon
}


}