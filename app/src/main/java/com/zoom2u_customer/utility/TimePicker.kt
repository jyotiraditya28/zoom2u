package com.zoom2u_customer.utility

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.aigestudio.wheelpicker.WheelPicker
import com.zoom2u_customer.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimePicker() : WheelPicker.OnItemSelectedListener {
    private lateinit var mContext: Context
    private var isSelectTimeWindow = 0
    private var dialogTimePicker: Dialog? = null
    private lateinit var wheelHr: WheelPicker
    private lateinit var wheelMin: WheelPicker
    private lateinit var wheelAmPm: WheelPicker
    private var selectedHr: String? = null
    private var selectedMin: String? = null
    private var selectedAmPm: String? = null
    private val hrArray =
        arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
    private var minArray: MutableList<String> = ArrayList()
    private val amPmArray = arrayOf("AM", "PM")

    fun timePickerDialog(
        context: Context,
        time: String,
        onItemClick: (hr: String?,min: String?,am_pm: String?) -> Unit
    ) {
        selectedHr=time.split(":")[0]
        selectedMin=time.split(":")[1].split(" ")[0]
        selectedAmPm=time.split(":")[1].split(" ")[1]



        for (i  in 0..59) {
            if(i<10){
                minArray.add("0$i")
            }else
            minArray.add(i.toString())
        }


        mContext = context
        isSelectTimeWindow = 0

        dialogTimePicker?.let {
            if (it.isShowing) it.dismiss()
            dialogTimePicker = null
        }

        dialogTimePicker = Dialog(context, R.style.DialogSlideAnim)
        dialogTimePicker!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogTimePicker!!.setContentView(R.layout.dialog_time_picker)

        val window = dialogTimePicker!!.window!!
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val params = window.attributes
        params.gravity = Gravity.BOTTOM
        window.attributes = params

        val done = dialogTimePicker!!.findViewById(R.id.btn_Done_TimeSelect) as Button
        val cancel = dialogTimePicker!!.findViewById(R.id.btn_Cancel_TimeSelect) as Button
        wheelHr = dialogTimePicker!!.findViewById(R.id.hr_picker) as WheelPicker
        wheelMin = dialogTimePicker!!.findViewById(R.id.min_picker) as WheelPicker
        wheelAmPm = dialogTimePicker!!.findViewById(R.id.am_pm_picker) as WheelPicker

        wheelHr.setOnItemSelectedListener(this)
        wheelMin.setOnItemSelectedListener(this)
        wheelAmPm.setOnItemSelectedListener(this)

        wheelHr.data = hrArray.toMutableList()
        wheelHr.selectedItemPosition = hrArray.indexOf(selectedHr)
        //wheelHr.selectedItemTextColor = mContext.resources.getColor(R.color.color_cyan)

        wheelMin.data = minArray
        wheelHr.selectedItemPosition = hrArray.indexOf(selectedMin)

        wheelAmPm.data = amPmArray.toMutableList()
        wheelHr.selectedItemPosition = hrArray.indexOf(selectedAmPm)




        done.setOnClickListener {
            onItemClick(selectedHr,selectedMin,selectedAmPm)
            dialogTimePicker!!.dismiss()
        }

        cancel.setOnClickListener {
            dialogTimePicker!!.dismiss()
        }

        dialogTimePicker!!.show()
    }


    override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
        when (picker) {
            wheelHr -> {
                wheelHr.selectedItemTextColor = mContext.resources.getColor(R.color.color_cyan)
                selectedHr = hrArray[position]
            }
            wheelMin -> {
                wheelMin.selectedItemTextColor = mContext.resources.getColor(R.color.color_cyan)
                selectedMin = minArray[position]
            }
            wheelAmPm -> {
                wheelAmPm.selectedItemTextColor = mContext.resources.getColor(R.color.color_cyan)
                selectedAmPm = amPmArray[position]
            }
        }


    }
}