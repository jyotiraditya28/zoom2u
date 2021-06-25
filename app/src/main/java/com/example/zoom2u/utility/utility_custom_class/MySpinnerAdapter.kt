package com.example.zoom2u.utility.utility_custom_class

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.zoom2u.R

class MySpinnerAdapter(var context: Context?, var counting: MutableList<String>) : BaseAdapter() {

    private var inflator: LayoutInflater? = null
    private var mCounting: MutableList<String>

    init {
        inflator = LayoutInflater.from(context)
        mCounting = counting
    }

    override fun getCount(): Int {
        return mCounting.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val view = inflator?.inflate(R.layout.spinner_item_layout,null)
        val tv: TextView?= view?.findViewById(R.id.spinner_textview)
        tv?.text = mCounting?.get(position)

        return view
    }
/*
    override fun getDropDownView(position: Int, convert: View, parent: ViewGroup?): View? {
    val convertView :View? = super.getDropDownView(position, convert, parent)
        convertView?.visibility = View.VISIBLE
        var p = convertView?.layoutParams
        if (p != null) {
            p.width = ViewGroup.LayoutParams.MATCH_PARENT
            p.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else p = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        convertView?.layoutParams = p
        return convertView
    }*/
}