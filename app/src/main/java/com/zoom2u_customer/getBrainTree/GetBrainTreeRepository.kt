package com.zoom2u_customer.getBrainTree

import android.content.Context
import android.widget.Toast
import com.google.gson.stream.MalformedJsonException
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException

class GetBrainTreeRepository(private var serviceApi: ServiceApi, var context: Context?)  {

    fun getBrainTreeToken(disposable: CompositeDisposable = CompositeDisposable(),
                       onSuccess: (token:String) -> Unit) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.brainTreeApiCall(
                    "api/Braintree/BrainTreeToken",
                    AppUtility.getApiHeaders()
                ).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<String>>() {
                        override fun onSuccess(responce: Response<String>) {
                            if (responce.body() != null) {

                                try {
                                    onSuccess(responce.body().toString())
                                } catch (e: MalformedJsonException) {

                                }
                            } else if (responce.errorBody() != null) {
                                AppUtility.progressBarDissMiss()
                                Toast.makeText(
                                    context,
                                    "something went wrong please try again.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
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


        /********** WebService to Get Braintree Client token  */
        fun getBrainTreeClientToken(context: Context?,onSuccess: (token:String) -> Unit) {
            var clientTokenId: String? = "0"
            try {
                val url =
                    URL( "https://api-test.zoom2u.com/api/Braintree/BrainTreeToken")
                clientTokenId = httpGetRequest(url)
                onSuccess(clientTokenId.toString())

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                AppUtility.progressBarDissMiss()
                DialogActivity.alertDialogSingleButton(
                    context,
                    "No Network !",
                    "No network connection, Please try again later."
                )
            }

        }

    }


    /********* Server call for GET Request  */
    private fun httpGetRequest(url: URL): String {
        var httpGetResponseStr = ""
        try {
            var conn: HttpURLConnection? = null
            conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty(
                "Authorization",
        AppUtility.getApiHeaders().get("authorization")
            )
            conn.connectTimeout = 30000
            conn.readTimeout = 40000
            conn.connect()
            // Get the server response
            var br: BufferedReader? = BufferedReader(InputStreamReader(conn.inputStream))
            var sb: StringBuilder? = StringBuilder()
            var line: String? = null
            while (br!!.readLine().also { line = it } != null) {
                // Append server response in string
                sb!!.append(line)
            }
            httpGetResponseStr = sb.toString()
            sb = null
            br = null
            conn.disconnect()
        } catch (e: UnknownHostException) {
            e.printStackTrace()

            httpGetResponseStr = ""
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()

            httpGetResponseStr = ""
        } catch (e: FileNotFoundException) {
            e.printStackTrace()

            httpGetResponseStr = ""
        } catch (e: Exception) {
            e.printStackTrace()

            httpGetResponseStr = ""
        }
        return httpGetResponseStr

    }

