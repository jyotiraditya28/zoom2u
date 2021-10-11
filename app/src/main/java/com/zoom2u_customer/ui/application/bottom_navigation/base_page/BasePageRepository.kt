package com.zoom2u_customer.ui.application.bottom_navigation.base_page

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.google.gson.Gson
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

class BasePageRepository(private var serviceApi: ServiceApi, var context: Context?) {


    fun sendDeviceTokenID(
        lat: String,
        lang: String,
        token: String,
        disposable: CompositeDisposable = CompositeDisposable(),

    ) {
        if (AppUtility.isInternetConnected()) {
            val locationStr: String = "$lat,$lang"
            if (!TextUtils.isEmpty(locationStr)) {
                disposable.add(
                    serviceApi.postWithJsonObject(
                        "breeze/customer/UpdateCustomerDeviceId?deviceId=$token&deviceType=Android&location=$locationStr",
                        AppUtility.getApiHeaders()
                    ).subscribeOn(
                        Schedulers.io()
                    )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                            override fun onSuccess(responce: Response<JsonObject>) {
                                if (responce.body() != null)
                                   /* Toast.makeText(
                                        context,
                                        "Device token send",
                                        Toast.LENGTH_LONG
                                    ).show()*/
                                else if (responce.errorBody() != null) {
                                    AppUtility.progressBarDissMiss()
                                    if(responce.code()==401){
                                       /* DialogActivity.alertDialogOnSessionExpire(
                                            context,
                                            onItemClick = ::onOkClick)*/
                                    }
                                    else
                                    Toast.makeText(context, "something went wrong please try again.", Toast.LENGTH_LONG).show()
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
            }

        }
        else {
           /* DialogActivity.alertDialogSingleButton(
                context,
                "No Network !",
                "No network connection, Please try again later."
            )*/
        }
    }



    fun sendDeviceTokenIDWithOutLocation(
        token: String,
        disposable: CompositeDisposable = CompositeDisposable(),

        ) {
        if (AppUtility.isInternetConnected()) {
            disposable.add(
                    serviceApi.postWithJsonObject(
                        "breeze/customer/UpdateCustomerDeviceId?deviceId=$token&deviceType=Android",
                        AppUtility.getApiHeaders()
                    ).subscribeOn(
                        Schedulers.io()
                    )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                            override fun onSuccess(responce: Response<JsonObject>) {
                                if (responce.body() != null)
                                   /* Toast.makeText(
                                        context,
                                        "Device token send",
                                        Toast.LENGTH_LONG
                                    ).show()*/
                                else if (responce.errorBody() != null) {
                                    AppUtility.progressBarDissMiss()
                                    if(responce.code()==401){
                                   /* DialogActivity.alertDialogOnSessionExpire(
                                        context,
                                        onItemClick = ::onOkClick)*/
                                }
                               else
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
            }
        else {
         /*   DialogActivity.alertDialogSingleButton(
                context,
                "No Network !",
                "No network connection, Please try again later."
            )*/
        }
    }

    private fun onOkClick(){
        AppUtility.onLogoutCall(context)
    }
}