package com.example.zoom2u.application.ui.details_base_page.profile.edit_profile

import android.content.Context
import android.util.Log
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.application.ui.details_base_page.history.HistoryResponse
import com.example.zoom2u.application.ui.details_base_page.profile.ProfileResponse
import com.example.zoom2u.utility.AppUtility
import com.example.zoom2u.utility.DialogActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class EditProfileRepository (private var serviceApi: ServiceApi, var context: Context?){

    fun setProflie(profileResponse: ProfileResponse?,
        disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (msg :String) -> Unit
    ) {
        if (AppUtility.isInternetConnected()) {7
           AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.postBodyJsonArray("breeze/customer/UpdateCustomer", AppUtility.getApiHeaders(),AppUtility.getJsonObject(Gson().toJson(profileResponse)))
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
            DialogActivity.alertDialogView(
                context,
                "No Network !",
                "No network connection, Please try again later."
            )
        }
    }
}