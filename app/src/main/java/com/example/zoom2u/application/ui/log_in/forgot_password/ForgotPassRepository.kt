package com.example.zoom2u.application.ui.log_in.forgot_password

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.utility.AppUtility
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.*

class ForgotPassRepository (private var serviceApi: ServiceApi,private var context: Context,private var onResponseCallback:(String,String) -> Unit){

    fun setForgotPass(username :String,
        disposable: CompositeDisposable = CompositeDisposable()) {
        AppUtility.progressBarShow(context)
        var request: HashMap<String, String> = HashMap<String, String>()
        request["username"] = username

        disposable.add(
            serviceApi.reSetPassword("api/account/forgotPassword?username=$username").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                    override fun onSuccess(responce: Response<JsonObject>) {
                        if (responce.body() != null) {
                            onResponseCallback("true",username)


                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("", "")

                    }


                })
        )

    }


}