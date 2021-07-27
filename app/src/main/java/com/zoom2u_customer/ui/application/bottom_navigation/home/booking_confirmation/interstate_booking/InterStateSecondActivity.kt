package com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.interstate_booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.braintreepayments.api.BraintreePaymentActivity
import com.braintreepayments.api.models.PaymentMethodNonce
import com.google.gson.Gson
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityInterStateSecondBinding
import com.zoom2u_customer.databinding.ActivityPricingPaymentBinding
import com.zoom2u_customer.getBrainTree.GetBrainTreeClientTokenOrBookDeliveryRequest
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.BookingConfirmationRepository
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.BookingConfirmationViewModel
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.BookingResponse
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.order_confirm_hold.OnHoldActivity
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.order_confirm_hold.OrderConfirmActivity
import com.zoom2u_customer.ui.application.bottom_navigation.home.pricing_payment.PricePaymentAdapter
import com.zoom2u_customer.utility.AppUtility
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class InterStateSecondActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityInterStateSecondBinding
    private var Request_Code = 1001
    private var repository: BookingConfirmationRepository? = null
    lateinit var viewModel: BookingConfirmationViewModel
    private var getBrainTreeClientToken: GetBrainTreeClientTokenOrBookDeliveryRequest? = null
    private var bookingDeliveryResponse: JSONObject? = null
    private var isFName:Boolean?=null
    private var isLName:Boolean?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_inter_state_second)

        if (intent.hasExtra("MainJsonForMakeABooking")) {
            bookingDeliveryResponse = JSONObject(intent.getStringExtra("MainJsonForMakeABooking"))
        }
        viewModel = ViewModelProvider(this).get(BookingConfirmationViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = BookingConfirmationRepository(serviceApi, this)
        viewModel.repository = repository

        setAdapterView(binding)
        viewModel.getDeliverySuccess()?.observe(this) {
            if (it != null) {
                AppUtility.progressBarDissMiss()
                if (it.isNotEmpty()) {
                    val bookingResponse: BookingResponse =
                        Gson().fromJson(it, BookingResponse::class.java)
                    if (!bookingResponse.`$type`
                            .equals("System.Web.Http.HttpError, System.Web.Http", ignoreCase = true)
                    ) {
                        if (bookingResponse.Verified == true) {
                            val loginPage = Intent(this, OrderConfirmActivity::class.java)
                            loginPage.putExtra(
                                "BookingResponse", bookingResponse
                            )
                            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(loginPage)
                            finish()
                        } else {
                            val intentOnHold = Intent(this, OnHoldActivity::class.java)
                            intentOnHold.putExtra("BookingResponse", bookingResponse)
                            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intentOnHold)
                            finish()
                        }
                    }
                }
            }
        }

        binding.chkTerms.setOnClickListener(this)
        binding.chkTerms1.setOnClickListener(this)
        binding.chkTerms2.setOnClickListener(this)
        binding.chkTerms3.setOnClickListener(this)
        binding.chkTerms4.setOnClickListener(this)
        binding.chkTerms5.setOnClickListener(this)
        binding.acceptBtn.setOnClickListener(this)
        binding.cancelBtn.setOnClickListener(this)

        binding.fName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.isNullOrEmpty()){
                  isFName=true
                    enableAcceptBtn(isFName,isLName,binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked
                        ,binding.chkTerms3.isChecked,binding.chkTerms4.isChecked,binding.chkTerms5.isChecked)
                }
                else{
                    isFName=false
                    enableAcceptBtn(isFName,isLName,binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked
                        ,binding.chkTerms3.isChecked,binding.chkTerms4.isChecked,binding.chkTerms5.isChecked)
                }
            }
        })

        binding.lName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.isNullOrEmpty()){
                    isLName=true
                    enableAcceptBtn(isFName,isLName,binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked
                        ,binding.chkTerms3.isChecked,binding.chkTerms4.isChecked,binding.chkTerms5.isChecked)
                }
                else{
                    isLName=false
                    enableAcceptBtn(isFName,isLName,binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked
                        ,binding.chkTerms3.isChecked,binding.chkTerms4.isChecked,binding.chkTerms5.isChecked)
                }
            }
        })
    }
    fun setAdapterView(binding: ActivityInterStateSecondBinding) {
        val layoutManager = GridLayoutManager(this, 3)
        binding.dangerView.layoutManager = layoutManager
        val adapter = DangerIconAdapter(this, DangerIconDataProvider.iconList)
        binding.dangerView.adapter = adapter

    }

    private fun callServiceForBookingRequest() {
        try {
            if (bookingDeliveryResponse!!.getJSONObject("_deliveryRequestModel")
                    .has("ETA")
            ) bookingDeliveryResponse!!.getJSONObject("_deliveryRequestModel").remove("ETA")
            getBrainTreeClientToken = GetBrainTreeClientTokenOrBookDeliveryRequest(
                this,
                Request_Code
            )
            bookingDeliveryResponse!!.getJSONObject("_deliveryRequestModel")
                .put("DeclarationSignature", binding.fName.text.toString().trim()+" "+
                        binding.lName.text.toString().trim())


            /*if (MainActivity.customerAccountType.equals("0") || MainActivity.customerAccountType.equals(
                    "Standard"
                )
            ) */
            getBrainTreeClientToken?.callServiceForGetClientToken()
            /*   else {
                 try {
                      jObjForConfirmation!!.getJSONObject("_deliveryRequestModel")
                          .put("paymentNonce", "")
                      getBrainTreeClientToken.callServiceForBookDeliveryRequest(jObjForConfirmation)
                      getBrainTreeClientToken = null
                  } catch (e: JSONException) {
                      e.printStackTrace()
                  }
            }    */
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Request_Code) {
            if (resultCode == RESULT_OK) {
                val paymentMethodNonce: PaymentMethodNonce? =
                    data?.getParcelableExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE)
                val nonce = paymentMethodNonce?.nonce
                try {
                    bookingDeliveryResponse!!.getJSONObject("_deliveryRequestModel")
                        .put("paymentNonce", nonce)
                    viewModel.getDeliveryRequest(bookingDeliveryResponse)
                    getBrainTreeClientToken = null
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.acceptBtn -> {
               if(enableAcceptBtn(isFName,isLName,binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked
                       ,binding.chkTerms3.isChecked,binding.chkTerms4.isChecked,binding.chkTerms5.isChecked))
                callServiceForBookingRequest()
            }
            R.id.chk_terms -> {
                enableAcceptBtn(isFName,isLName,binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked
                    ,binding.chkTerms3.isChecked,binding.chkTerms4.isChecked,binding.chkTerms5.isChecked)
            }
            R.id.chk_terms1 -> {
                enableAcceptBtn(isFName,isLName,binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked
                ,binding.chkTerms3.isChecked,binding.chkTerms4.isChecked,binding.chkTerms5.isChecked)
            }
            R.id.chk_terms2 -> {
                enableAcceptBtn(isFName,isLName,binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked
                    ,binding.chkTerms3.isChecked,binding.chkTerms4.isChecked,binding.chkTerms5.isChecked)
            }
            R.id.chk_terms3 -> {
                enableAcceptBtn(isFName,isLName,binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked
                    ,binding.chkTerms3.isChecked,binding.chkTerms4.isChecked,binding.chkTerms5.isChecked)
            }
            R.id.chk_terms4 -> {
                enableAcceptBtn(isFName,isLName,binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked
                    ,binding.chkTerms3.isChecked,binding.chkTerms4.isChecked,binding.chkTerms5.isChecked)
            }
            R.id.chk_terms5 -> {
                enableAcceptBtn(isFName,isLName,binding.chkTerms.isChecked,binding.chkTerms1.isChecked,binding.chkTerms2.isChecked
                    ,binding.chkTerms3.isChecked,binding.chkTerms4.isChecked,binding.chkTerms5.isChecked)
            }
            R.id.cancelBtn->{
                finish()
            }

        }

    }
    private fun enableAcceptBtn(isFName:Boolean?,isLName:Boolean?,checkTerm: Boolean?, checkTerm1: Boolean?, checkTerm2: Boolean?
        , checkTerm3: Boolean?, checkTerm4: Boolean?, checkTerm5: Boolean?) :Boolean{
        if(isFName ==true&& isLName ==true&& checkTerm==true && checkTerm1==true && checkTerm2==true && checkTerm3==true && checkTerm4==true && checkTerm5==true){
            binding.acceptBtn.setBackgroundResource(R.drawable.chip_background)
            return true
        }else
            binding.acceptBtn.setBackgroundResource(R.drawable.gray_background)

        return false
    }
}