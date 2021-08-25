package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class CompletedBidListRepository(private var serviceApi: ServiceApi, var context: Context?) {

    fun getCompletedBidList(page:Int, disposable: CompositeDisposable = CompositeDisposable(),
                       onSuccess: (history:List<CompletedBidListResponse>) -> Unit) {
        if (AppUtility.isInternetConnected()) {
            // AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.getWithJsonObject(
                    "breeze/HeavyFreight/GetAllRequests?page=$page&type=completed&searchText=",
                    AppUtility.getApiHeaders()
                ).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null) {
                                val convert =
                                    Gson().toJson(responce.body()?.get("data")?.asJsonArray)
                                val listType = object : TypeToken<List<CompletedBidListResponse?>?>() {}.type
                                val list: List<CompletedBidListResponse> =
                                    Gson().fromJson(convert, listType)
                                onSuccess(list)

                            }
                            else if (responce.errorBody() != null) {
                                AppUtility.progressBarDissMiss()
                                if(responce.code()==401){
                                    DialogActivity.alertDialogOnSessionExpire(
                                        context,
                                        onItemClick = ::onOkClick)
                                }
                                else{
                                    Toast.makeText(context, "Something went wrong please try again.", Toast.LENGTH_LONG).show() }

                            }
                        }
                        private fun onOkClick(){
                            AppUtility.onLogoutCall(context)
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