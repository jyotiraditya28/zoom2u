package com.zoom2u_customer.utility

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.maps.MapFragment

class WorkaroundMapFragment : MapFragment() {
    private var mListener: OnTouchListener? = null
    override fun onCreateView(
        layoutInflater: LayoutInflater?,
        viewGroup: ViewGroup?,
        savedInstance: Bundle?
    ): View? {
        var layout: View? = null
        try {
            layout = super.onCreateView(layoutInflater, viewGroup, savedInstance)
            val frameLayout: TouchableWrapper = TouchableWrapper(activity)
            frameLayout.setBackgroundColor(resources.getColor(R.color.transparent))
            (layout as ViewGroup?)!!.addView(
                frameLayout,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return layout
    }

    fun setListener(listener: OnTouchListener?): WorkaroundMapFragment? {
        mListener = listener
        return null
    }

    interface OnTouchListener {
        fun onTouch()
    }

    inner class TouchableWrapper(context: Context?) :
        FrameLayout(context!!) {
        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
            try {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> mListener!!.onTouch()
                    MotionEvent.ACTION_UP -> mListener!!.onTouch()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return super.dispatchTouchEvent(event)
        }
    }
}