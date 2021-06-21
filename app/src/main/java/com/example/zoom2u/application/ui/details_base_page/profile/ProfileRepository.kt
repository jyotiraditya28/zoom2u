package com.example.zoom2u.application.ui.details_base_page.profile

import android.util.Log
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.utility.AppUtility
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class ProfileRepository(private var serviceApi: ServiceApi,private var onResponseCallback:(ProfileResponce) -> Unit) {

    fun getProflie(disposable: CompositeDisposable = CompositeDisposable()) {
        //if (AppUtility.isInternetConnected()) {

        //AppUtility.progressBarShow(context)


        disposable.add(
            serviceApi.zoom2uCall1("breeze/customer/Customers",AppUtility.getApiHeaders()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response<JsonArray>>() {
                    override fun onSuccess(responce: Response<JsonArray>) {
                        if (responce.body() != null) {

                            val listType = object : TypeToken<List<ProfileResponce?>?>() {}.type
                            val list: List<ProfileResponce> =
                                Gson().fromJson<List<ProfileResponce>>(responce.body(), listType)
                            onResponseCallback(list.get(0))

                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("", "")
                    }
                }))
    }
}