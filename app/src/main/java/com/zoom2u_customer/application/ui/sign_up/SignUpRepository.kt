package com.zoom2u_customer.application.ui.sign_up

import android.content.Context
import android.util.Log

import com.google.gson.JsonObject
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.HashMap

class SignUpRepository(
    private var serviceApi: ServiceApi, var context: Context,
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
            DialogActivity.alertDialogSingleButton(
                context,
                "No Network !",
                "No network connection, Please try again later."
            )
        }
    }
}
