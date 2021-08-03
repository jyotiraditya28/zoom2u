package com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.interstate_booking


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ActivityInterStateFirstScreenBinding
import org.json.JSONObject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


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

        val text =  getString(R.string.the_appropriate_label_has_been_attached_label_for_laptops_and_mobile_phones_can_be_downloaded_here)
        val spannableString = SpannableString(text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color=resources.getColor(R.color.base_color)
                ds.isUnderlineText = false
            }

            override fun onClick(p0: View) {
                Toast.makeText(this@InterStateFirstScreen,"hera",Toast.LENGTH_LONG).show()
            }
        }
        spannableString.setSpan(clickableSpan, text.length-7, text.length-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.laptopText.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.laptopText.movementMethod = LinkMovementMethod.getInstance()



        if (bookingDeliveryResponse!!.getJSONObject("_deliveryRequestModel")
                .getString("isLaptopOrMobile").toString()=="laptopOrMobileYes"
        ){
            binding.isLaptopMobile.visibility=View.VISIBLE
            binding.laptopText.visibility=View.VISIBLE
        }else{
            binding.isLaptopMobile.visibility=View.GONE
            binding.laptopText.visibility=View.GONE
        }
    }

    
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.acceptBtn -> {
                if (enableAcceptBtn(binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked)) {
                    val intent = Intent(this, InterStateSecondActivity::class.java)
                    intent.putExtra("MainJsonForMakeABooking", bookingDeliveryResponse.toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
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