package com.example.zoom2u.application.ui.details_base_page.profile.chnage_password

import android.content.Context
import android.util.Log
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.utility.AppUtility
import com.google.gson.JsonObject
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