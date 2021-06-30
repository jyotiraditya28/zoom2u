package com.zoom2u_customer.application.ui.details_base_page.profile.my_location.edit_add_location

import android.content.Context
import android.util.Log

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.application.ui.details_base_page.profile.my_location.model.AddLocationReq
import com.zoom2u_customer.application.ui.details_base_page.profile.my_location.model.MyLocationResAndEditLocationReq
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class EditAddLocationRepository(private var serviceApi: ServiceApi, var context: Context) {

    fun editLocation(
        myLocationResponse: MyLocationResAndEditLocationReq?,
        disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (msg: String) -> Unit
    ) {
        if (AppUtility.isInternetConnected()) {

            AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.postBodyJsonObject(
                    "breeze/customer/SavePreferredLocations",
                    AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(Gson().toJson(myLocationResponse))
                ).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null) {
                                onSuccess(Gson().toJson(responce.body()))
                            } else if (responce.errorBody() != null)
                                onSuccess("")
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

    fun addLocation(addLocationReq: AddLocationReq?, disposable: CompositeDisposable = CompositeDisposable(),
                    onSuccess: (msg: String) -> Unit) {

        if (AppUtility.isInternetConnected()) {

             AppUtility.progressBarShow(context)
                disposable.add(
                    serviceApi.postBodyJsonObject(
                        "breeze/customer/SavePreferredLocations",
                        AppUtility.getApiHeaders(),
                        AppUtility.getJsonObject(Gson().toJson(addLocationReq))
                    ).subscribeOn(
                        Schedulers.io()
                    )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                            override fun onSuccess(responce: Response<JsonObject>) {
                                if (responce.body() != null) {
                                    onSuccess(Gson().toJson(responce.body()))
                                } else if (responce.errorBody() != null)
                                    onSuccess("")
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

    fun deleteLocation(locationId:Int?, disposable: CompositeDisposable = CompositeDisposable(),
                       onSuccess: (msg: String) -> Unit ){

        if (AppUtility.isInternetConnected()) {

            AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.postWithJsonObject(
                    "breeze/customer/DeleteCustomerPreferredLocation?locationId=$locationId",
                    AppUtility.getApiHeaders())
                    .subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null) {
                                onSuccess(Gson().toJson(responce.body()))
                            } else if (responce.errorBody() != null)
                                onSuccess("")
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