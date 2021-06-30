package com.zoom2u_customer.application.ui.details_base_page.profile.edit_profile

import android.content.Context
import android.util.Log

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.application.ui.details_base_page.profile.ProfileResponse
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class EditProfileRepository (private var serviceApi: ServiceApi, var context: Context?){

    fun setProfile(profileResponse: ProfileResponse?,
                   disposable: CompositeDisposable = CompositeDisposable(),
                   onSuccess: (msg :String) -> Unit
    ) {
        if (AppUtility.isInternetConnected()) {7
           AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.postBodyJsonObject("breeze/customer/UpdateCustomer", AppUtility.getApiHeaders(),AppUtility.getJsonObject(Gson().toJson(profileResponse)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null) {
                                onSuccess(Gson().toJson(responce.body()))
                            }else if(responce.errorBody()!=null)
                                onSuccess("")
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

   /* fun uploadDb(profileImage: Bitmap,
                 disposable: CompositeDisposable = CompositeDisposable(),
                 onSuccess: (msg :String) -> Unit): {

    }*/
}