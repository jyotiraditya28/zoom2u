package com.zoom2u_customer.ui.application.bottom_navigation.history.history_details

import android.content.Context
import android.util.Log
import android.widget.Toast

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.ui.application.bottom_navigation.history.HistoryResponse
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class HistoryDetailsRepository(private var serviceApi: ServiceApi, var context: Context?) {

    fun getHistoryDetails(bookingref:String?,disposable: CompositeDisposable = CompositeDisposable(),
                       onSuccess: (history:HistoryDetailsResponse) -> Unit) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.getWithJsonArray(
                    "breeze/customer/DeliveriesDetailsById?bookingRef=$bookingref",
                    AppUtility.getApiHeaders()
                ).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonArray>>() {
                        override fun onSuccess(responce: Response<JsonArray>) {
                            if (responce.body() != null) {
                                val listType =
                                    object : TypeToken<List<HistoryDetailsResponse>?>() {}.type
                                val list: List<HistoryDetailsResponse> =
                                    Gson().fromJson(responce.body(), listType)
                                onSuccess(list[0])

                            }
                            else if (responce.errorBody() != null) {
                                AppUtility.progressBarDissMiss()
                                if(responce.code()==401){
                                    DialogActivity.logoutDialog(
                                        context,
                                        "Confirm!",
                                        "Your token has expired you have to login again.",
                                        "Ok","Cancel",
                                        onCancelClick=::onCancelClick,
                                        onOkClick = ::onOkClick
                                    )
                                }
                                else{
                                    Toast.makeText(context, "Error Code:${responce.code()} something went wrong please try again.", Toast.LENGTH_LONG).show() }

                            }
                        }
                        private fun onOkClick(){
                            AppUtility.onLogoutCall(context)
                        }

                        private fun onCancelClick(){

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


    fun cancelBooking(
        historyItem: HistoryResponse?,
        disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (historyItem: HistoryResponse) -> Unit
    ) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.cancelBooking(
                    "breeze/customer/CancelBooking?bookingId=${historyItem?.BookingId}",
                    AppUtility.getApiHeaders()
                ).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<Void>>() {
                        override fun onSuccess(responce: Response<Void>) {
                            if (historyItem != null) {
                                onSuccess(historyItem)
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

    fun cancelBooking(
        bookingId: String?,
        disposable: CompositeDisposable = CompositeDisposable(),
        onSuccessMAB: (bookingId: String) -> Unit
    ) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.cancelBooking(
                    "breeze/customer/CancelBooking?bookingId=${bookingId}",
                    AppUtility.getApiHeaders()
                ).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<Void>>() {
                        override fun onSuccess(responce: Response<Void>) {
                            onSuccessMAB("true")
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