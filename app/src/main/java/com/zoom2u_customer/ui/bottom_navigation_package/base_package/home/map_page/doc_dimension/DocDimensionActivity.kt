package com.zoom2u_customer.ui.bottom_navigation_package.base_package.home.map_page.doc_dimension

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
import com.zoom2u_customer.ui.bottom_navigation_package.base_package.home.home_fragment.Icon

class DocDimensionActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityDocDimensionBinding
    private var icon: Icon? = null
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


        binding.confirmBtn.setOnClickListener(this)

    }


    fun getTotalWeight(quantity: Int?, weight: Double?) {
        if (quantity != null && weight != null) {
            binding.totalWeight.text = (quantity * weight).toString()+" "+"Kg"
            binding.totalWeight1.text = "Total Weight = "+(quantity * weight).toString()+"Kg"


        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.confirm_btn -> {
                updateIconData()

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
            Toast.makeText(this,"Profile details updated successfully.", Toast.LENGTH_LONG).show()
            finish()
        }


    }
}