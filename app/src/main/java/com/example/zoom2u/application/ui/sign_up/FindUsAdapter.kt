package com.example.zoom2u.application.ui.sign_up

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.zoom2u.R
import java.util.*

class FindUsAdapter(context: Context,layoutResourceId:Int,findus:List<String>) :
    ArrayAdapter<String>(context, R.layout.custom_dropdoen_item) {
/*
    var layoutResourceId = 0
    var contextMylocationAdapter: Context? = null
    var arrayOfLocationPickUpDelivery: ArrayList<String>? = null
    private var itemsAll: ArrayList<String>? = null
    private var suggestions: ArrayList<String>? = null


    init {
        this.layoutResourceId = layoutResourceId
        contextMylocationAdapter = contextMylocationAdapter
        arrayOfLocationPickUpDelivery = arrayOfLocationPickUpDelivery
        itemsAll = arrayOfLocationPickUpDelivery!!.clone() as ArrayList<String>?
        suggestions = ArrayList()
    }



    fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var v = convertView
        if (v == null) {
            val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = vi.inflate(layoutResourceId, null)
        }
        val pickOrDropModel: String = arrayOfLocationPickUpDelivery.get(position)
        if (pickOrDropModel != null) {
            val customerNameLabel = v!!.findViewById<TextView>(R.id.item_title_name)
            if (customerNameLabel != null) {

                customerNameLabel.setText(pickOrDropModel)
            }
        }
        return v
    }*/


}