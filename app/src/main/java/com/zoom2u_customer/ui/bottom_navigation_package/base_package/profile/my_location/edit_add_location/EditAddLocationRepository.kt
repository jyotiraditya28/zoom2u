package com.zoom2u_customer.ui.bottom_navigation_package.base_package.profile.my_location.edit_add_location

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zoom2u_customer.apiclient.GoogleServiceApi
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.ui.bottom_navigation_package.base_package.profile.my_location.model.AddLocationReq
import com.zoom2u_customer.ui.bottom_navigation_package.base_package.profile.my_location.model.MyLocationResAndEditLocationReq
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import com.zoom2u_customer.utility.Zoom2uContractProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.net.URLEncoder
import java.util.*

class EditAddLocationRepository(
    private var serviceApi: ServiceApi,
    private var googleServiceApi: GoogleServiceApi,
    var context: Context
) {

    fun editLocation(
        myLocationResponse: MyLocationResAndEditLocationReq?,
        disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (msg: String) -> Unit
    ) {
        if (AppUtility.isInternetConnected()) {

            AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.postBodyJsonObject(
                    "breeze/customer/SavePreferredLocations",
                    AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(Gson().toJson(myLocationResponse))
                ).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null) {
                                onSuccess(Gson().toJson(responce.body()))
                            } else if (responce.errorBody() != null) {
                                AppUtility.progressBarDissMiss()
                                Toast.makeText(context, "something went wrong please try again.", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onError(e: Throwable) {
                            AppUtility.progressBarDissMiss()
                            Log.d("", "")
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

    fun addLocation(
        addLocationReq: AddLocationReq?, disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (msg: String) -> Unit
    ) {

        if (AppUtility.isInternetConnected()) {

            AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.postBodyJsonObject(
                    "breeze/customer/SavePreferredLocations",
                    AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(Gson().toJson(addLocationReq))
                ).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null) {
                                onSuccess(Gson().toJson(responce.body()))
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
                            Log.d("", "")
                            Toast.makeText(context, "something went wrong please try again.", Toast.LENGTH_LONG).show()
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

    fun deleteLocation(
        locationId: Int?, disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (msg: String) -> Unit
    ) {

        if (AppUtility.isInternetConnected()) {

            AppUtility.progressBarShow(context)
            disposable.add(
                serviceApi.postWithJsonObject(
                    "breeze/customer/DeleteCustomerPreferredLocation?locationId=$locationId",
                    AppUtility.getApiHeaders()
                )
                    .subscribeOn(
                        Schedulers.io()
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null) {
                                onSuccess(Gson().toJson(responce.body()))
                            } else if (responce.errorBody() != null) {
                                AppUtility.progressBarDissMiss()
                                Toast.makeText(context, "something went wrong please try again.", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onError(e: Throwable) {
                            AppUtility.progressBarDissMiss()
                            Log.d("", "")
                            Toast.makeText(context, "something went wrong please try again.", Toast.LENGTH_LONG).show()
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


    fun getAddressFromGeocoder(
        address: String?,
        isTrue: Boolean?,
        disposable: CompositeDisposable = CompositeDisposable(),
        onSuccess: (add:HashMap<String, Any>,isTrue:Boolean?)-> Unit
    ) {
        if (AppUtility.isInternetConnected()) {
            val urlEncodedAddress = URLEncoder.encode(address, "utf8")
            disposable.add(
                googleServiceApi.getAddressFromGeocoder(
                    "geocode/json?&address=$urlEncodedAddress&key=${Zoom2uContractProvider.API_KEY_GEOCODER_DIRECTION}"
                ).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<JsonObject>>() {
                        override fun onSuccess(responce: Response<JsonObject>) {
                            if (responce.body() != null) {


                                val mapForAllAddressInformation = HashMap<String, Any>()
                                try {
                                    // Create a JSON object hierarchy from the results
                                    val jsonObj = JSONObject(responce.body().toString())
                                    if (jsonObj.getString("status") == "OK") {
                                        val resultsJsonArray: JSONArray =
                                            jsonObj.getJSONArray("results")
                                        var formatted_address = "-NA-"

                                        // Extracting formatted address, if available
                                        if (!resultsJsonArray.getJSONObject(0)
                                                .isNull("formatted_address")
                                        ) {
                                            // formatted_address = resultsJsonArray.getJSONObject(0).getString("formatted_address");
                                            formatted_address = address.toString()
                                        }
                                        val latStr = resultsJsonArray.getJSONObject(0)
                                            .getJSONObject("geometry").getJSONObject("location")
                                            .getDouble("lat")
                                        val longStr = resultsJsonArray.getJSONObject(0)
                                            .getJSONObject("geometry").getJSONObject("location")
                                            .getDouble("lng")
                                        mapForAllAddressInformation["address"] = formatted_address
                                        mapForAllAddressInformation["latitude"] = latStr
                                        mapForAllAddressInformation["longitude"] = longStr
                                        val jArrayOfAddress_Component =
                                            resultsJsonArray.getJSONObject(0)
                                                .getJSONArray("address_components")
                                        if (jArrayOfAddress_Component.length() > 0) {
                                            for (j in 0 until jArrayOfAddress_Component.length()) {
                                                val jsonArr =
                                                    jArrayOfAddress_Component.getJSONObject(j)
                                                        .getJSONArray("types")
                                                try {
                                                    if (j == 0) {
                                                        if (jsonArr.length() == 0) {
                                                            mapForAllAddressInformation["defaultName"] =
                                                                jArrayOfAddress_Component.getJSONObject(
                                                                    j
                                                                ).getString("long_name")
                                                        }
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                    mapForAllAddressInformation["defaultName"] = ""
                                                }
                                                try {
                                                    if (jsonArr.getString(0) == "street_number") {
                                                        mapForAllAddressInformation["streetNumber"] =
                                                            jArrayOfAddress_Component.getJSONObject(
                                                                j
                                                            ).getString("short_name")
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                    mapForAllAddressInformation["streetNumber"] =
                                                        " "
                                                }
                                                try {
                                                    if (jsonArr.getString(0) == "sublocality") {
                                                        mapForAllAddressInformation["sublocality"] =
                                                            jArrayOfAddress_Component.getJSONObject(
                                                                j
                                                            ).getString("long_name")
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                    mapForAllAddressInformation["sublocality"] = " "
                                                }
                                                try {
                                                    if (jsonArr.getString(0) == "route") {
                                                        mapForAllAddressInformation["route"] =
                                                            jArrayOfAddress_Component.getJSONObject(
                                                                j
                                                            ).getString("long_name")
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                    mapForAllAddressInformation["route"] = " "
                                                }
                                                try {
                                                    if (jsonArr.getString(0) == "colloquial_area") {
                                                        mapForAllAddressInformation["suburb"] =
                                                            jArrayOfAddress_Component.getJSONObject(
                                                                j
                                                            ).getString("short_name")
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                    mapForAllAddressInformation["suburb"] = " "
                                                }
                                                try {
                                                    if (jsonArr.getString(0) == "locality") {
                                                        mapForAllAddressInformation["suburb"] = jArrayOfAddress_Component.getJSONObject(j).getString("short_name")
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                    mapForAllAddressInformation["suburb"] = " "
                                                }
                                                try {
                                                    if (jsonArr.getString(0) == "administrative_area_level_1") {
                                                        mapForAllAddressInformation["state"] =
                                                            jArrayOfAddress_Component.getJSONObject(
                                                                j
                                                            ).getString("short_name")
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                    mapForAllAddressInformation["state"] = " "
                                                }
                                                try {
                                                    if (jsonArr.getString(0) == "administrative_area_level_1") {
                                                        mapForAllAddressInformation["stateLong"] =
                                                            jArrayOfAddress_Component.getJSONObject(
                                                                j
                                                            ).getString("long_name")
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                    mapForAllAddressInformation["stateLong"] = " "
                                                }
                                                try {
                                                    if (jsonArr.getString(0) == "country") {
                                                        mapForAllAddressInformation["country"] =
                                                            jArrayOfAddress_Component.getJSONObject(
                                                                j
                                                            ).getString("long_name")
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                    mapForAllAddressInformation["country"] = " "
                                                }
                                                try {
                                                    if (jsonArr.getString(0) == "postal_code") {
                                                        mapForAllAddressInformation["postcode"] =
                                                            jArrayOfAddress_Component.getJSONObject(
                                                                j
                                                            ).getString("short_name")
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                    mapForAllAddressInformation["postcode"] = " "
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception) {

                                }



                                onSuccess(mapForAllAddressInformation,isTrue)




                            } else if (responce.errorBody() != null){
                                AppUtility.progressBarDissMiss()
                                Toast.makeText(context, "something went wrong please try again.", Toast.LENGTH_LONG).show()
                            }

                        }

                        override fun onError(e: Throwable) {
                            AppUtility.progressBarDissMiss()
                            Log.d("", "")
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