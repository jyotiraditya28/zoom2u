package com.zoom2u_customer.apiclient

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {
        private val BaseUrl = "https://api-test.zoom2u.com/"
        private lateinit var retrofit: Retrofit

        private fun getInstance(): Retrofit {

            com.zoom2u_customer.apiclient.ApiClient.Companion.retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    com.zoom2u_customer.apiclient.ApiClient.Companion.initOkHttpBuilder().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(com.zoom2u_customer.apiclient.ApiClient.Companion.BaseUrl)
                .build()

            return com.zoom2u_customer.apiclient.ApiClient.Companion.retrofit
        }

        private fun initOkHttpBuilder(): OkHttpClient.Builder {
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(60, TimeUnit.SECONDS)
            builder.readTimeout(60, TimeUnit.SECONDS)
            return builder
        }

        fun getServices(): com.zoom2u_customer.apiclient.ServiceApi {
            return com.zoom2u_customer.apiclient.ApiClient.Companion.getInstance()
                .create(com.zoom2u_customer.apiclient.ServiceApi::class.java)
        }
    }
}