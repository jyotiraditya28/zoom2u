package com.example.zoom2u.application.ui.sign_up

import android.content.Context
import android.util.Log
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.application.ui.log_in.LoginRequest
import com.example.zoom2u.application.ui.log_in.LoginResponce
import com.example.zoom2u.utility.AppUtility
import com.example.zoom2u.utility.DialogActivity
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.HashMap

class SignUpRepository(
    private var serviceApi: ServiceApi,var context: Context,
    private var onResponseCallback: (String) -> Unit
) {

    fun getSignUpFromRepo(signUpRequest: SignUpRequest, disposable: CompositeDisposable = CompositeDisposable()) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(context)
            var request: HashMap<String, String> = HashMap<String, String>()
            request.put("firstName", signUpRequest.firstName)
            request.put("lastName", signUpRequest.lastName)
            request.put("company", signUpRequest.company)
            request.put("userName", signUpRequest.userName)
            request.put("mobile", signUpRequest.mobile)
            request.put("howDidYouFindUs", signUpRequest.howDidYouFindUs)
            request.put("password", signUpRequest.password)
            request.put("confirmPassword", signUpRequest.confirmPassword)
            request.put("terms", signUpRequest.terms)
            request.put("courier", signUpRequest.courier)
            request.put("customerType", signUpRequest.customerType)
            disposable.add(
                serviceApi.getSignUpData(request).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null) {
                                if (responce.body()!!.get("success").getAsBoolean())
                                    onResponseCallback("true")

                            }
                        }

                        override fun onError(e: Throwable) {
                            Log.d("", "")

                        }


                    })
            )
        }else {
            DialogActivity.alertDialogView(
                context,
                "No Network !",
                "No network connection, Please try again later."
            )
        }
    }
}
