package com.zoom2u_customer.ui.application.base_package.home.booking_confirmation

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.Response

class BookingConfirmationRepository(private var serviceApi: ServiceApi, var context: Context?) {

    fun getSaveDeliveryRequest(
        jObjForPlaceBooking: JSONObject?,
        disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (msg: String) -> Unit
    ) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.postBodyJsonObject(
                    "breeze/customer/SaveDeliveryRequest",
                    AppUtility.getApiHeaders(),
                    JsonParser.parseString(jObjForPlaceBooking.toString()) as JsonObject
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null)
                                onSuccess(Gson().toJson(responce.body()))
                            else if (responce.errorBody() != null) {
                                AppUtility.progressBarDissMiss()
                                Toast.makeText(
                                    context,
                                    "something went wrong please try again.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }

                        override fun onError(e: Throwable) {
                            AppUtility.progressBarDissMiss()
                            Toast.makeText(
                                context,
                                "something went wrong please try again.",
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