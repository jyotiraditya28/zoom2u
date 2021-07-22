package com.zoom2u_customer.ui.application.bottom_navigation.home.map_page.doc_dimension

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ActivityDocDimensionBinding
import com.zoom2u_customer.ui.application.bottom_navigation.home.home_fragment.Icon
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity

class DocDimensionActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityDocDimensionBinding
    private var icon: Icon? = null
    var length:Int?=null
    var height:Int?=null
    var widht:Int?=null
    var totalWight:Double?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_doc_dimension)

        if (intent.hasExtra("Icon")) {
            icon = intent.getParcelableExtra("Icon")
        }

        binding.quantity.setText(icon?.quantity.toString())
        binding.itemWeight.setText(icon?.weight.toString())
        binding.length.setText(icon?.length.toString())
        binding.width.setText(icon?.width.toString())
        binding.height.setText(icon?.height.toString())
        length=icon?.length
        height=icon?.height
        widht=icon?.width
        getTotalWeight(icon?.quantity,icon?.weight)


        binding.quantity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "" && binding.itemWeight.text.toString() != "")
                    getTotalWeight(
                        s.toString().toInt(),
                        binding.itemWeight.text.toString().toDouble()
                    )
                else if (s.toString() == "") {
                    binding.totalWeight.text = "0.0" + " " + "Kg"
                    binding.totalWeight1.text = "Total Weight = 0.0Kg"
                }
            }
        })


        binding.itemWeight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "" && binding.quantity.text.toString() != "")
                    getTotalWeight(
                        binding.quantity.text.toString().toInt(),
                        s.toString().toDouble()
                    )
                else if (s.toString() == "") {
                    binding.totalWeight.text = "0.0" + " " + "Kg"
                    binding.totalWeight1.text = "Total Weight = 0.0Kg"

                }
            }
        })



        binding.length.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                   length=s.toString().toInt()
                    if (length!! > 200) {
                        binding.errorText.visibility = View.VISIBLE
                    } else
                        binding.errorText.visibility = View.GONE
                }
            }
        })


        binding.height.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    height=s.toString().toInt()
                    if (height!! > 200) {
                        binding.errorText.visibility = View.VISIBLE
                    } else
                        binding.errorText.visibility = View.GONE
                }
            }
        })

        binding.width.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    widht=s.toString().toInt()
                    if (widht!! > 200) {
                        binding.errorText.visibility = View.VISIBLE
                    } else
                        binding.errorText.visibility = View.GONE
                }
            }
        })




        binding.confirmBtn.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
    }


    fun getTotalWeight(quantity: Int?, weight: Double?) {
        if (quantity != null && weight != null) {
            totalWight=quantity * weight
            binding.totalWeight.text = (totalWight).toString()+" "+"Kg"
            binding.totalWeight1.text = "Total Weight = "+(totalWight).toString()+"Kg"

            if(totalWight!! >100)
                binding.errorText.visibility=View.VISIBLE
            else
                binding.errorText.visibility=View.GONE

        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.confirm_btn -> {
               if(checkValidation(binding.quantity.text?.trim().toString(),
                       binding.itemWeight.text?.trim().toString(),
                       binding.length.text?.trim().toString(),
                       binding.height.text?.trim().toString(),
                       binding.width.text?.trim().toString())) {

                   if (totalWight!! > 100 || length!! > 200 || height!! > 200 || widht!! > 200)
                       DialogActivity.alertDialogSingleButton(
                           this,
                           "Warning!",
                           "Items weighing over 30kg each or 100kg in total will need to be placed through our Bid Request services. The same goes for items with measurements exceeding 200cm and multiple number of items exceeding the limit per booking. This is to maintain the safety and good health of our drivers."
                       )
                   else
                       updateIconData()
               }
            }
            R.id.back_btn->{
                finish()
            }
        }
    }

    private fun updateIconData() {
        icon?.quantity = binding.quantity.text?.trim().toString().toInt()
        icon?.weight = binding.itemWeight.text?.trim().toString().toDouble()
        icon?.length = binding.length.text?.trim().toString().toInt()
        icon?.width = binding.width.text?.trim().toString().toInt()
        icon?.height = binding.height.text?.trim().toString().toInt()

        if(icon!=null){
            val intent = Intent()
            intent.putExtra("Icon", icon)
            setResult(1, intent)
            Toast.makeText(this,"Item details updated successfully.", Toast.LENGTH_LONG).show()
            finish()
        }


    }


    private fun checkValidation(quantity: String, weight: String,  length: String,height: String, width: String): Boolean {
        when {
            quantity == "" -> {
                Toast.makeText(this,"Please enter item quantity.",Toast.LENGTH_LONG).show()
                return false
            }
            weight=="" -> {
                Toast.makeText(this,"Please enter item weight.",Toast.LENGTH_LONG).show()
                return false
            }
            length == "" -> {
                Toast.makeText(this,"Please enter item length.",Toast.LENGTH_LONG).show()
                return false
            }
            width=="" -> {
                Toast.makeText(this,"Please enter item width.",Toast.LENGTH_LONG).show()
                return false
            }
            height == "" -> {
                Toast.makeText(this,"Please enter item height.",Toast.LENGTH_LONG).show()
                return false
            }
            else -> return true
        }
    }
}