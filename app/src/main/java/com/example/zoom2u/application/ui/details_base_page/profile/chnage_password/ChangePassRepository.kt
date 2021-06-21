package com.example.zoom2u.application.ui.details_base_page.profile.chnage_password

import android.util.Log
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.application.ui.sign_up.SignUpResponce
import com.example.zoom2u.utility.AppUtility
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class ChangePassRepository(private var serviceApi: ServiceApi, private var onResponseCallback: (String) -> Unit) {

    fun changePass(oldPass:String, newPass :String, disposable: CompositeDisposable = CompositeDisposable()) {
        //if (AppUtility.isInternetConnected()) {

        //AppUtility.progressBarShow(context)


        disposable.add(
            serviceApi.zoom2uCall("api/account/changePassword?OldPassword=$oldPass&NewPassword=$newPass", AppUtility.getApiHeaders()).subscribeOn(
                Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                    override fun onSuccess(responce: Response<JsonObject>) {
                        if (responce.body() != null) {
                            if (responce.body()!!.get("success").getAsBoolean()){
                                onResponseCallback("true")
                        }
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("", "")
                    }
                }))
    }
}