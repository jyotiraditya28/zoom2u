package com.example.zoom2u.application.ui.details_base_page.history

import android.content.Context
import android.util.Log
import com.example.zoom2u.apiclient.ServiceApi
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

class HistoryRepository(private var serviceApi: ServiceApi, var context: Context?) {

    fun getHistoryList(disposable: CompositeDisposable = CompositeDisposable(),
                       onSuccess: (history:List<HistoryResponse>) -> Unit) {
        if (AppUtility.isInternetConnected()) {
          AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.getWithJsonObject(
                    "breeze/customer/DeliveriesForCustomer?currentPage=1&searchText=",
                    AppUtility.getApiHeaders()
                ).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null) {
                                val convert =
                                    Gson().toJson(responce.body()?.get("data")?.getAsJsonArray())
                                val listType = object : TypeToken<List<HistoryResponse?>?>() {}.type
                                val list: List<HistoryResponse> =
                                    Gson().fromJson(convert, listType)
                                onSuccess(list)

                            }
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