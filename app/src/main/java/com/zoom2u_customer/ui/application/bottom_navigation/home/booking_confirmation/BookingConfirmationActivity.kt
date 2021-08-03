package com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.braintreepayments.api.BraintreePaymentActivity
import com.braintreepayments.api.models.PaymentMethodNonce
import com.google.gson.Gson
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityBookingConfirmationBinding
import com.zoom2u_customer.getBrainTree.GetBrainTreeClientTokenOrBookDeliveryRequest
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.interstate_booking.InterStateFirstScreen

import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.order_confirm_hold.OnHoldActivity
import com.zoom2u_customer.ui.application.bottom_navigation.home.booking_confirmation.order_confirm_hold.OrderConfirmActivity
import com.zoom2u_customer.ui.application.bottom_navigation.home.home_fragment.Icon
import com.zoom2u_customer.utility.AppUtility

import org.json.JSONException
import org.json.JSONObject
import java.util.*

class BookingConfirmationActivity : AppCompatActivity(), View.OnClickListener {
    private var bookingDeliveryResponse: JSONObject? = null
    private var Request_Code = 1001
    private var getBrainTreeClientToken: GetBrainTreeClientTokenOrBookDeliveryRequest? = null
    lateinit var binding: ActivityBookingConfirmationBinding
    private lateinit var itemDataList: ArrayList<Icon>
    private lateinit var adapter: BookingPackageAdapter
    lateinit var viewModel: BookingConfirmationViewModel
    private var repository: BookingConfirmationRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_confirmation)

        viewModel = ViewModelProvider(this).get(BookingConfirmationViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = BookingConfirmationRepository(serviceApi, this)
        viewModel.repository = repository

        /**get data from map Item*/
        if (intent.hasExtra("IconList")) {
            itemDataList = intent.getParcelableArrayListExtra<Icon>("IconList") as ArrayList<Icon>
            bookingDeliveryResponse = JSONObject(intent.getStringExtra("MainJsonForMakeABooking"))
            setDataView(bookingDeliveryResponse)
        }
        setAdapterView()
        binding.bookingConfirmation.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
        binding.bookingConfirmation.isEnabled = true
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
                            intent.flags=Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(loginPage)
                            finish()
                        } else {
                            val intentOnHold = Intent(this, OnHoldActivity::class.java)
                            intentOnHold.putExtra("BookingResponse", bookingResponse)
                            intent.flags=Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intentOnHold)
                            finish()
                        }
                    }
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setDataView(bookingRequest: JSONObject?) {

        binding.time.text = bookingRequest?.getJSONObject("_deliveryRequestModel")?.getString("ETA")
        binding.totalPrice.text =
            "$" + (bookingRequest?.getJSONObject("_deliveryRequestModel")?.getInt("Price") as Int +
                    bookingRequest.getJSONObject("_deliveryRequestModel").getInt("BookingFee"))

        binding.pickName.text =
            bookingRequest.getJSONObject("_deliveryRequestModel").getJSONObject("PickupLocation")
                .getString("ContactName")
        binding.pickAdd.text =
            bookingRequest.getJSONObject("_deliveryRequestModel").getJSONObject("PickupLocation")
                .getString("Address")
        binding.dropName.text =
            bookingRequest.getJSONObject("_deliveryRequestModel").getJSONObject("DropLocation")
                .getString("ContactName")
        binding.dropAdd.text =
            bookingRequest.getJSONObject("_deliveryRequestModel").getJSONObject("DropLocation")
                .getString("Address")

    }


    fun setAdapterView() {
        val layoutManager = GridLayoutManager(this, 1)
        binding.iconView.layoutManager = layoutManager
        adapter = BookingPackageAdapter(this, itemDataList)
        binding.iconView.adapter = adapter
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.booking_confirmation -> {
                binding.bookingConfirmation.isClickable=false
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.bookingConfirmation.isClickable=true

                }, 3000)

                if (binding.chkTerms.isChecked) {
                    bookingConfirmation()
                }else{
                    Toast.makeText(this,"Please Accept the customer Terms and Conditions.", Toast.LENGTH_LONG).show()
                    binding.bookingConfirmation.isClickable=true
                }
            }
            R.id.back_btn->{
                finish()
            }
        }
    }


    private fun callServiceForBookingRequest() {
       AppUtility.progressBarShow(this)
        try {
            if (bookingDeliveryResponse!!.getJSONObject("_deliveryRequestModel")
                    .has("ETA")
            ) bookingDeliveryResponse!!.getJSONObject("_deliveryRequestModel").remove("ETA")
            getBrainTreeClientToken = GetBrainTreeClientTokenOrBookDeliveryRequest(
                this,
                Request_Code
            )
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


    private fun bookingConfirmation() {

        try {
            if (bookingDeliveryResponse!!.getJSONObject("_deliveryRequestModel")
                    .getBoolean("IsInterstate")
            ) {
                if (bookingDeliveryResponse!!.getJSONObject("_deliveryRequestModel")
                        .getString("DeliverySpeed") == "Interstate" || bookingDeliveryResponse!!.getJSONObject(
                        "_deliveryRequestModel"
                    ).getString("DeliverySpeed") == "Road interstate"
                ) {

                    if (bookingDeliveryResponse!!.getJSONObject("_deliveryRequestModel")
                            .getString("DeliverySpeed") == "Interstate"
                    ) {
                        val intent = Intent(this, InterStateFirstScreen::class.java)
                        intent.putExtra(
                            "MainJsonForMakeABooking",
                            bookingDeliveryResponse.toString()
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)


                    } else if (bookingDeliveryResponse!!.getJSONObject("_deliveryRequestModel")
                            .getString("DeliverySpeed") == "Road interstate"
                    ) {
                        //interStateHeaderTxt.setText(R.string.interstateAlertHeaderroadinterstate)
                        //interStateMsgTxt.setText(R.string.interstateMsgTxtroadInterstate)
                    }




                } else
                    callServiceForBookingRequest()
            } else callServiceForBookingRequest()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


}