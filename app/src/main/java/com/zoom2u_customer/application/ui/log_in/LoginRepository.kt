package com.zoom2u_customer.application.ui.log_in

import android.content.Context
import android.util.Log

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.utility.AppPreference
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.*

class LoginRepository(private var serviceApi: ServiceApi, var context: Context) {


    fun getLoginFromRepo(
        loginRequest: LoginRequest,
        disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (msg: String) -> Unit
    ) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(context)
            var request: HashMap<String, String> = HashMap<String, String>()
            request.put("grant_type", loginRequest.grant_type)
            request.put("username", loginRequest.username)
            request.put("password", loginRequest.password)
            request.put("isDeliveriesPortal", loginRequest.isDeliveriesPortal)

            disposable.add(
                serviceApi.getLoginData(request).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {

                            if (responce.body() != null) {
                                AppPreference.getSharedPrefInstance()
                                    .setLoginResponse(Gson().toJson(responce.body()))
                                onSuccess("true")
                            } else if (responce.errorBody() != null) {
                                onSuccess("Username password combination is incorrect. Check for spelling errors, spaces in email password, or automatic capitalisation")
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