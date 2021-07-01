package com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile

import android.content.Context
import android.util.Log

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class ProfileRepository(private var serviceApi: ServiceApi, var context: Context?) {

    fun getProflie(
        disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (profile: ProfileResponse) -> Unit
    ) {
        if (AppUtility.isInternetConnected()) {

            // AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.getWithJsonArray("breeze/customer/Customers", AppUtility.getApiHeaders())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonArray>>() {
                        override fun onSuccess(responce: Response<JsonArray>) {
                            if (responce.body() != null) {

                                val listType = object : TypeToken<List<ProfileResponse?>?>() {}.type
                                val list: List<ProfileResponse> =
                                    Gson().fromJson(responce.body(), listType)
                                onSuccess(list[0])
                                //AppPreference.getSharedPrefInstance().setLoginResponse(Gson().toJson(list[0]))

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