package com.zoom2u_customer.ui.application.base_package.home.booking_confirmation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.zoom2u_customer.ui.application.base_package.home.delivery_details.model.SaveDeliveryRequestReq
import com.zoom2u_customer.ui.application.base_package.home.home_fragment.Icon
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class BookingConfirmationActivity : AppCompatActivity(), View.OnClickListener {
    private var jObjForConfirmation: JSONObject? = null
    private var REQUEST_CODE = 1001
    var jObjForBookingRequest: JSONObject? = null
    var getBrainTreeClientToken: GetBrainTreeClientTokenOrBookDeliveryRequest? = null
    lateinit var binding: ActivityBookingConfirmationBinding
    private lateinit var itemDataList: ArrayList<Icon>
    private lateinit var adapter: BookingPackageAdapter
    lateinit var viewModel: BookingConfirmationViewModel
    private var repository: BookingConfirmationRepository? = null
    private var saveDeliveryRequestReq: SaveDeliveryRequestReq? = null
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
            jObjForConfirmation = JSONObject(intent.getStringExtra("MainJsonForMakeABooking"))
            setDataView(jObjForConfirmation)
            //saveDeliveryRequestReq = intent.getParcelableExtra("SaveDeliveryRequestReq")


        }

        setAdapterView()

        binding.bookingConfirmation.setOnClickListener(this)

        viewModel.getDeliverySuccess()?.observe(this) {
            if (it != null) {
                AppUtility.progressBarDissMiss()
                if (it.isNotEmpty()) {
                    val bookingResponse: BookingResponse = Gson().fromJson(it, BookingResponse::class.java)
                    if (!bookingResponse.`$type`
                            .equals("System.Web.Http.HttpError, System.Web.Http", ignoreCase = true)
                    ) {
                        if (bookingResponse.Verified==true) {
                            val loginPage = Intent(this, OrderConfirmActivity::class.java)
                            loginPage.putExtra(
                                "BookingResponse", bookingResponse)
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

        binding.time.text=bookingRequest?.getJSONObject("_deliveryRequestModel")?.getString("ETA")
        binding.totalPrice.text="$" + (bookingRequest?.getJSONObject("_deliveryRequestModel")?.getInt("Price") as Int +
                bookingRequest.getJSONObject("_deliveryRequestModel").getInt("BookingFee") )

        binding.pickName.text = bookingRequest.getJSONObject("_deliveryRequestModel").getJSONObject("PickupLocation").getString("ContactName")
        binding.pickAdd.text = bookingRequest.getJSONObject("_deliveryRequestModel").getJSONObject("PickupLocation").getString("Address")
        binding.dropName.text = bookingRequest.getJSONObject("_deliveryRequestModel").getJSONObject("DropLocation").getString("ContactName")
        binding.dropAdd.text = bookingRequest.getJSONObject("_deliveryRequestModel").getJSONObject("DropLocation").getString("Address")

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
                //viewModel.getDeliveryRequest( saveDeliveryRequestReq)
                callServiceForBookingRequest()
            }
        }
    }


    private fun callServiceForBookingRequest() {
        try {
            if (jObjForConfirmation!!.getJSONObject("_deliveryRequestModel")
                    .has("ETA")
            ) jObjForConfirmation!!.getJSONObject("_deliveryRequestModel").remove("ETA")
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
                    jObjForConfirmation!!.getJSONObject("_deliveryRequestModel")
                        .put("paymentNonce", nonce)
                    callServiceForBookDeliveryRequest(jObjForConfirmation)
                    getBrainTreeClientToken = null
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun callServiceForBookDeliveryRequest(jObjForPlaceBooking: JSONObject?) {
        try {
            jObjForBookingRequest = jObjForPlaceBooking
            /*  var vehicleSTr = ""
              vehicleSTr =
                  when (jObjForBookingRequest!!.getJSONObject("_deliveryRequestModel")
                      .getString("Vehicle")) {
                      "Bike" -> "0"
                      "Car" -> "1"
                      "Van" -> "2"
                      else -> "1"
                  }*/
            // jObjForBookingRequest.getJSONObject("_deliveryRequestModel").put("DropDateTime", MainActivity.functionalUtilityObj.getDropDateTimeFromDeviceToServer(jObjForBookingRequest.getJSONObject("_deliveryRequestModel").getString("DropDateTime")));
            jObjForBookingRequest!!.getJSONObject("_deliveryRequestModel").put(
                "DropDateTime",
                jObjForBookingRequest!!.getJSONObject("_deliveryRequestModel")
                    .getString("DropDateTime")
            )
            jObjForBookingRequest!!.getJSONObject("_deliveryRequestModel")
                .put("Vehicle", "Bike")
            jObjForBookingRequest!!.getJSONObject("_deliveryRequestModel")
                .put("sendSmsToPickupPerson", false)

            viewModel.getDeliveryRequest(jObjForBookingRequest)


        } catch (e: JSONException) {
            e.printStackTrace()
            DialogActivity.alertDialogSingleButton(
                this,
                "Sorry!",
                "Something went wrong, Please try again"
            )
        }
    }

}