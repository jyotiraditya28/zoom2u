package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.active_bid_offers

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonObject
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class ActiveBidOffersRepository(private var serviceApi: ServiceApi, var context: Context?) {


    fun quotePayment(
        nonce: String,
        requestId: String,
        offerId: String,
        orderNo:String,
        disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (msg: String) -> Unit
    ) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.postWithJsonObject(
                    "breeze/ExtraLargeQuoteRequest/AcceptQuoteOffer?requestId=$requestId&offerId=$offerId&paymentNonce=$nonce&purchaseOrderNumber=$orderNo",
                    AppUtility.getApiHeaders()
                ).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null)
                                onSuccess(responce.body()!!.get("BookingRef").toString())
                            else if (responce.errorBody() != null) {
                                AppUtility.progressBarDissMiss()
                                DialogActivity.alertDialogSingleButton(
                                    context,
                                    "Sorry!",
                                    "May be you entered wrong password, Please try again"
                                )
                            }
                        }

                        override fun onError(e: Throwable) {
                            AppUtility.progressBarDissMiss()
                            Toast.makeText(
                                context,
                                R.string.signup_error_msg.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            )
        } else {
            DialogActivity.alertDialogSingleButton(
                context,
                "No Network !",
                "No network connection, Please try again later."
            )
        }
    }
}