package com.zoom2u_customer.getBrainTree

import android.app.Activity
import android.content.Context
import android.widget.RelativeLayout
import com.braintreepayments.api.PaymentRequest
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import org.json.JSONException
import org.json.JSONObject

class GetBrainTreeClientTokenOrBookDeliveryRequest(var context: Context,var requestCode:Int) {

    private var repository: GetBrainTreeRepository? = null
    var requestCod:Int=0
    var subLayout: RelativeLayout? = null

    init {
        this.requestCod=requestCode
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = GetBrainTreeRepository(serviceApi,context)

    }


    fun callServiceForGetClientToken() {
        //repository?.getBrainTreeToken(onSuccess = ::onTokenSuccess)
        val thread = Thread {
            try {
                repository?.getBrainTreeClientToken(context,onSuccess = ::onTokenSuccess)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }

    private fun onTokenSuccess(BrainToken:String) {
        AppUtility.progressBarDissMiss()
        if (BrainToken != "" || BrainToken != "0") {
            val paymentRequest: PaymentRequest = PaymentRequest().clientToken(BrainToken)
            (context as Activity).startActivityForResult(
                paymentRequest.getIntent(
                    context
                ), requestCode
            )
        } else DialogActivity.alertDialogSingleButton(
            context,
            "Sorry!",
            "We can't process your booking at this time, please try again"
        )
    }



}