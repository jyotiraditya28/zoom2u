package com.example.zoom2u.apiclient


import com.example.zoom2u.application.ui.log_in.LoginResponce
import com.example.zoom2u.application.ui.sign_up.SignUpRequest
import com.example.zoom2u.application.ui.sign_up.SignUpResponce
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Single

import retrofit2.Response
import retrofit2.http.*


interface ServiceApi {

    @FormUrlEncoded
    @POST("token")
    fun getLoginData(@FieldMap stringMap: Map<String, String> ):Single<Response<JsonObject>>


    @POST("api/account/registerCustomer")
    fun getSignUpData(@Body stringMap: Map<String, String>  ):Single<Response<JsonObject>>

    @POST
    fun reSetPassword(@Url url:String):Single<Response<JsonObject>>

    @POST
    fun postWithJsonObject(@Url url:String, @HeaderMap  map:Map<String, String>):Single<Response<JsonObject>>

    @GET
    fun getWithJsonObject(@Url url:String, @HeaderMap  map:Map<String, String>):Single<Response<JsonObject>>

    @GET
    fun getWithJsonArray(@Url url:String, @HeaderMap  map:Map<String, String>):Single<Response<JsonArray>>

    @POST
    fun postBodyJsonArray(@Url url:String, @HeaderMap  map:Map<String, String>, @Body request:JsonObject?):Single<Response<JsonObject>>

    @POST
    fun postWithJsonBody(@Url url:String ,@HeaderMap  map:Map<String, String>,@Body request:JsonObject?):Single<Response<JsonArray>>
}
