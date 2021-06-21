package com.example.zoom2u.apiclient

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {
        private val BaseUrl = "https://api-staging.zoom2u.com/"
        private lateinit var retrofit: Retrofit

        private fun getInstance(): Retrofit {

            retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(initOkHttpBuilder().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BaseUrl)
                .build()

            return retrofit
        }

        private fun initOkHttpBuilder(): OkHttpClient.Builder {
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(60, TimeUnit.SECONDS)
            builder.readTimeout(60, TimeUnit.SECONDS)
            return builder
        }

        fun getServices(): ServiceApi {
            return getInstance().create(ServiceApi::class.java)
        }
    }
}