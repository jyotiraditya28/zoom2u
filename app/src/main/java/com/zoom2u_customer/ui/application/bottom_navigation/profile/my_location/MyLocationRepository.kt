package com.zoom2u_customer.ui.application.bottom_navigation.profile.my_location

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.ui.application.bottom_navigation.profile.my_location.model.MyLocationResAndEditLocationReq
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
            //CustomProgressBar.progressBarShow(context,"Loading...","Please wait a moment")
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
                               // CustomProgressBar.dismissProgressBar()
                                AppUtility.progressBarDissMiss()
                                onSuccess(list)
                            } else if (responce.errorBody() != null) {
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
                                    Toast.makeText(context, "Something went wrong please try again.", Toast.LENGTH_LONG).show() }

                            }
                        }
                        private fun onOkClick(){
                            AppUtility.onLogoutCall(context)
                        }

                        private fun onCancelClick(){

                        }


                        override fun onError(e: Throwable) {
                            //CustomProgressBar.dismissProgressBar()
                            AppUtility.progressBarDissMiss()
                            Toast.makeText(context, "Something went wrong please try again.", Toast.LENGTH_LONG).show()
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