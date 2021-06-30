package com.zoom2u_customer.application.ui.details_base_page.profile.my_location

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.application.ui.details_base_page.profile.my_location.model.MyLocationResAndEditLocationReq
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MyLocationRepository(private var serviceApi: ServiceApi, var context: Context){

    fun getMyLocation( disposable: CompositeDisposable = CompositeDisposable(),  onSuccess: (myLocationList:List<MyLocationResAndEditLocationReq>) -> Unit) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.getWithJsonArray(
                    "breeze/customer/GetPreferredLocations",
                    AppUtility.getApiHeaders()
                ).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonArray>>() {
                        override fun onSuccess(responce: Response<JsonArray>) {
                            if (responce.body() != null) {
                                val listType =
                                    object : TypeToken<List<MyLocationResAndEditLocationReq?>?>() {}.type
                                val list: List<MyLocationResAndEditLocationReq> =
                                    Gson().fromJson(responce.body(), listType)
                                AppUtility.progressBarDissMiss()
                                onSuccess(list)


                            }
                        }

                        override fun onError(e: Throwable) {
                            Log.d("", "")
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