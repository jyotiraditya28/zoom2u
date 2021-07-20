package com.zoom2u_customer.ui.application.base_package.home.booking_confirmation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
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
import com.zoom2u_customer.ui.application.base_package.home.booking_confirmation.order_confirm_hold.OnHoldActivity
import com.zoom2u_customer.ui.application.base_package.home.booking_confirmation.order_confirm_hold.OrderConfirmActivity
import com.zoom2u_customer.ui.application.base_package.home.home_fragment.Icon
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class BookingConfirmationActivity : AppCompatActivity(), View.OnClickListener {
    private var bookingDeliveryResponce: JSONObject? = null
    private var REQUEST_CODE = 1001
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
            bookingDeliveryResponce = JSONObject(intent.getStringExtra("MainJsonForMakeABooking"))
            setDataView(bookingDeliveryResponce)
        }

        setAdapterView()

        binding.bookingConfirmation.setOnClickListener(this)
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
                            startActivity(loginPage)
                            finish()
                        } else {
                            var intentOnHold = Intent(this, OnHoldActivity::class.java)
                            intentOnHold.putExtra("BookingResponse", bookingResponse)
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
                if (binding.chkTerms.isChecked) {
                    if (binding.bookingConfirmation.isEnabled) {
                        bookingConfirmation()
                        binding.bookingConfirmation.isEnabled = false
                    }
                }else{
                    Toast.makeText(this,"Please Accept the customer Terms and Conditions.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun callServiceForBookingRequest() {
        try {
            if (bookingDeliveryResponce!!.getJSONObject("_deliveryRequestModel")
                    .has("ETA")
            ) bookingDeliveryResponce!!.getJSONObject("_deliveryRequestModel").remove("ETA")
            getBrainTreeClientToken = GetBrainTreeClientTokenOrBookDeliveryRequest(
                this,
                REQUEST_CODE
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
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val paymentMethodNonce: PaymentMethodNonce? =
                    data?.getParcelableExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE)
                val nonce = paymentMethodNonce?.nonce
                try {
                    bookingDeliveryResponce!!.getJSONObject("_deliveryRequestModel")
                        .put("paymentNonce", nonce)
                    viewModel.getDeliveryRequest(bookingDeliveryResponce)
                    getBrainTreeClientToken = null
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun bookingConfirmation() {
        var interstateDialogForThankYou: Dialog? = null
        try {
            if (bookingDeliveryResponce!!.getJSONObject("_deliveryRequestModel")
                    .getBoolean("IsInterstate")
            ) {
                if (bookingDeliveryResponce!!.getJSONObject("_deliveryRequestModel")
                        .getString("DeliverySpeed") == "Interstate" || bookingDeliveryResponce!!.getJSONObject(
                        "_deliveryRequestModel"
                    ).getString("DeliverySpeed") == "Road interstate"
                ) {
                    if (interstateDialogForThankYou != null) interstateDialogForThankYou = null
                    interstateDialogForThankYou =
                        Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
                    interstateDialogForThankYou.setCancelable(false)
                    interstateDialogForThankYou.window
                        ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    interstateDialogForThankYou.setContentView(R.layout.alert_interstate)
                    val window: Window? = interstateDialogForThankYou.window
                    window?.setLayout(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    val wlp = window?.attributes
                    wlp?.gravity = Gravity.CENTER
                    wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv())
                    window?.attributes = wlp
                    val interStateHeaderTxt: TextView =
                        interstateDialogForThankYou.findViewById(R.id.interStateHeaderTxt)

                    val interStateMsgTxt: TextView =
                        interstateDialogForThankYou.findViewById(R.id.interStateMsgTxt)

                    if (bookingDeliveryResponce!!.getJSONObject("_deliveryRequestModel")
                            .getString("DeliverySpeed") == "Interstate"
                    ) {
                        interStateHeaderTxt.setText(R.string.interstateAlertHeader)
                        interStateMsgTxt.setText(R.string.interstateMsgTxt)
                    } else if (bookingDeliveryResponce!!.getJSONObject("_deliveryRequestModel")
                            .getString("DeliverySpeed") == "Road interstate"
                    ) {
                        interStateHeaderTxt.setText(R.string.interstateAlertHeaderroadinterstate)
                        interStateMsgTxt.setText(R.string.interstateMsgTxtroadInterstate)
                    }

                    val cancelBtnInterStateDialog: TextView =
                        interstateDialogForThankYou.findViewById(R.id.cancelBtnInterStateDialog)

                    val acceptBtnInterStateDialog: Button =
                        interstateDialogForThankYou.findViewById(R.id.acceptBtnInterStateDialog)

                    acceptBtnInterStateDialog.setOnClickListener {
                        interstateDialogForThankYou.dismiss()
                        callServiceForBookingRequest()
                    }
                    cancelBtnInterStateDialog.setOnClickListener { interstateDialogForThankYou.dismiss() }
                    interstateDialogForThankYou.show()
                } else callServiceForBookingRequest()
            } else callServiceForBookingRequest()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


}