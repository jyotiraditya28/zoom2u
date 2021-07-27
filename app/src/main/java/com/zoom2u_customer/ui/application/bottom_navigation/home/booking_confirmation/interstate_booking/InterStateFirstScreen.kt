package com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.interstate_booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ActivityBookingConfirmationBinding
import com.zoom2u_customer.databinding.ActivityInterStateFirstScreenBinding
import com.zoom2u_customer.getBrainTree.GetBrainTreeClientTokenOrBookDeliveryRequest
import com.zoom2u_customer.ui.application.bottom_navigation.home.home_fragment.Icon
import com.zoom2u_customer.ui.application.bottom_navigation.home.pricing_payment.PricingPaymentActivity
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class InterStateFirstScreen : AppCompatActivity() , View.OnClickListener{
    lateinit var binding: ActivityInterStateFirstScreenBinding
    private var bookingDeliveryResponse: JSONObject? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inter_state_first_screen)


        if (intent.hasExtra("MainJsonForMakeABooking")) {
            bookingDeliveryResponse = JSONObject(intent.getStringExtra("MainJsonForMakeABooking"))
        }
        binding.acceptBtn.setOnClickListener(this)
        binding.cancelBtn.setOnClickListener(this)
        binding.chkTerms.setOnClickListener(this)
        binding.chkTerms1.setOnClickListener(this)
        binding.chkTerms2.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.acceptBtn -> {
                if (enableAcceptBtn(binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked)) {
                    val intent = Intent(this, InterStateSecondActivity::class.java)
                    intent.putExtra("MainJsonForMakeABooking", bookingDeliveryResponse.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                }
            }
            R.id.chk_terms -> {
                enableAcceptBtn(binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked)

            }
            R.id.chk_terms1 -> {
                enableAcceptBtn(binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked)
            }
            R.id.chk_terms2 -> {
                enableAcceptBtn(binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked)
            }
            R.id.cancelBtn -> {
                finish()
            }

            }
    }

    private fun enableAcceptBtn(checkTerm: Boolean?, checkTerm1: Boolean?, checkTerm2: Boolean?) :Boolean{
        if(checkTerm==true&&checkTerm1==true&&checkTerm2==true){
            binding.acceptBtn.setBackgroundResource(R.drawable.chip_background)
            return true
        }else
            binding.acceptBtn.setBackgroundResource(R.drawable.gray_background)

     return false
    }


}