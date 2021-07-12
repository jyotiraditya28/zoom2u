package com.zoom2u_customer.ui.buttom_navigation_package.base_package.profile.chnage_password

import android.content.Context
import android.util.Log

import com.google.gson.JsonObject
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.utility.AppUtility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class ChangePassRepository(private var serviceApi: ServiceApi, var context: Context, private var onResponseCallback: (String) -> Unit) {

    fun changePass(oldPass:String, newPass :String, disposable: CompositeDisposable = CompositeDisposable()) {
        //if (AppUtility.isInternetConnected()) {
        AppUtility.progressBarShow(context)
        disposable.add(
            serviceApi.postWithJsonObject("api/account/changePassword?OldPassword=$oldPass&NewPassword=$newPass", AppUtility.getApiHeaders()).subscribeOn(
                Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                    override fun onSuccess(responce: Response<JsonObject>) {
                        if (responce.body() != null) {
                            if (responce.body()!!.get("success").getAsBoolean()){
                                onResponseCallback("true")
                        }else if(responce.errorBody()!=null){
                                onResponseCallback("Incorrect Password.")
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("", "")
                    }
                }))
    }
}