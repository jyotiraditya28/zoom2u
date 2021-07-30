package com.zoom2u_customer.ui.application.bottom_navigation.profile

import android.content.Context
import android.widget.Toast
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
                            else if (responce.errorBody() != null) {
                                AppUtility.progressBarDissMiss()
                                //TODO
                                Toast.makeText(
                                    context,
                                    "Error Code:${responce.code()} something went wrong please try again.",
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