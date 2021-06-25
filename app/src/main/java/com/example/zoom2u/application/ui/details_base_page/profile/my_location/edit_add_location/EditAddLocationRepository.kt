package com.example.zoom2u.application.ui.details_base_page.profile.my_location.edit_add_location

import android.content.Context
import android.util.Log
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.application.ui.details_base_page.profile.my_location.model.MyLocationResAndEditLocationReq
import com.example.zoom2u.utility.AppUtility
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class EditAddLocationRepository(private var serviceApi: ServiceApi, var context: Context) {

    fun getEditAddLocation(
        myLocationResponse: MyLocationResAndEditLocationReq?,
        disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (msg: String) -> Unit
    ) {
        //if (AppUtility.isInternetConnected()) {


        //  AppUtility.progressBarShow(context)
        disposable.add(
            serviceApi.postBodyJsonArray(
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
    }
}