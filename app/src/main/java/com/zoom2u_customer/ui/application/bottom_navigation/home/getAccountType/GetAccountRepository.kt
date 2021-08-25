package com.zoom2u_customer.ui.application.bottom_navigation.home.getAccountType

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.CompletedDetailsResponse
import com.zoom2u_customer.ui.application.bottom_navigation.profile.ProfileResponse
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class GetAccountRepository (private var serviceApi: ServiceApi, var context: Context?) {

    fun getAccountType(
        disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (accountType: AccountTypeModel) -> Unit
    ) {
        if (AppUtility.isInternetConnected()) {

            // AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.getWithJsonObject("breeze/customer/GetCustomerAccountType", AppUtility.getApiHeaders())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null) {
                                val bid: AccountTypeModel =
                                    Gson().fromJson(responce.body(),AccountTypeModel::class.java)
                                onSuccess(bid)

                            }
                            else if (responce.errorBody() != null) {
                                AppUtility.progressBarDissMiss()
                                if(responce.code()==401){
                                    DialogActivity.alertDialogOnSessionExpire(
                                        context,
                                        onItemClick = ::onOkClick)
                                }
                                else{
                                    Toast.makeText(context, "Something went wrong please try again.", Toast.LENGTH_LONG).show() }

                            }
                        }
                        private fun onOkClick(){
                            AppUtility.onLogoutCall(context)
                        }


                        override fun onError(e: Throwable) {
                            AppUtility.progressBarDissMiss()
                            Toast.makeText(
                                context,
                                "something went wrong please try again.",
                                Toast.LENGTH_LONG
                            ).show()
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
}