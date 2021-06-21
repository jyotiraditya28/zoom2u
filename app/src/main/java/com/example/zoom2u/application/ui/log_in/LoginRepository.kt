package com.example.zoom2u.application.ui.log_in

import android.content.Context
import android.util.Log
import androidx.core.content.res.TypedArrayUtils.getText
import com.example.zoom2u.R
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.utility.AppPreference
import com.example.zoom2u.utility.AppUtility
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.*

class LoginRepository(private var serviceApi: ServiceApi,var context: Context,private var onResponseCallback:(String) -> Unit) {


    fun getLoginFromRepo(loginRequest: LoginRequest,
                         disposable: CompositeDisposable = CompositeDisposable()
    ) {
        //if (AppUtility.isInternetConnected()) {

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

                        if(responce.body()!=null){
                            val gson = Gson()
                            val loginResponse :LoginResponce=gson.fromJson(responce.body(), LoginResponce::class.java)
                            AppPreference.getSharedPrefInstance().setLoginResponse(Gson().toJson(responce.body()))
                            onResponseCallback("true")
                        }else if(responce.errorBody()!=null){
                            onResponseCallback("Username password combination is incorrect. Check for spelling errors, spaces in email password, or automatic capitalisation")
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("", "")

                    }


                })
        )
        /*} else {
            retrofitCallback.showErrorMessage("Please check your internet connection")
        }*/
    }
}