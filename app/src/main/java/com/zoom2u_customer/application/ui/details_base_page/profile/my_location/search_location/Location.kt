package com.zoom2u_customer.application.ui.details_base_page.profile.my_location.search_location

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.zoom2u_customer.R
import com.zoom2u_customer.utility.utility_custom_class.MySpinnerAdapter
import java.util.*
import kotlin.collections.ArrayList

class Location : AppCompatActivity() {


    var sessionToken: AutocompleteSessionToken? = null

    private var placesClient: PlacesClient? = null
    //var spinner:Spinner? = null
    var arrayPfAddress_N_Fav: MutableList<Model_AddAddress_n_Fav> = ArrayList()
    var mAdapter: Adapter_AddAddress_n_Fav? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)







        if (!Places.isInitialized()) {
            val apiKey = this.getString(R.string.google_api_key)
            Places.initialize(this.applicationContext, apiKey)
        }
        sessionToken = AutocompleteSessionToken.newInstance()
        placesClient = Places.createClient(this)
        //spinner = findViewById(R.id.spinner)
        var recycler_AddAddressView =findViewById<RecyclerView>(R.id.recycler_AddAddressView)
        var edtWhereTo_AddAddressView = findViewById<EditText>(R.id.edtWhereTo_AddAddressView)
        var backBtn_AddAddressView = findViewById<ImageView>(R.id.backBtn_AddAddressView)
        var imageview_cross = findViewById<ImageView>(R.id.imageview_cross)
        var layoutParent = findViewById<RelativeLayout>(R.id.layoutParent)
        layoutParent.visibility= View.INVISIBLE
        recycler_AddAddressView.adapter = mAdapter
        Handler().postDelayed(object:Runnable{
            override fun run() {
                layoutParent.visibility= View.VISIBLE
            }
        },400)

        edtWhereTo_AddAddressView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.count() > 0) {
                    arrayPfAddress_N_Fav.clear()
                    placeAPICallForAddress(edtWhereTo_AddAddressView.text.toString())
                }
            }
        })


        var arrayPfAddress_N_Fav = ArrayList<Model_AddAddress_n_Fav>()
        mAdapter = Adapter_AddAddress_n_Fav(this, arrayPfAddress_N_Fav!!, object : Adapter_AddAddress_n_Fav.OnItemClickListener {
            override fun onItemClick(item: Model_AddAddress_n_Fav) {
                /*item_AddAddressAry = item
                edtWhereTo_AddAddressView!!.setText("")
                recycler_AddAddressView!!.visibility = View.GONE
                GetAddressDetails_GeoCoder().execute()*/

            /*    val addAddressIntent = Intent(context, AddStopDetail_Activity::class.java)
                addAddressIntent.putExtra("item_add_stop", item)
                //  addAddressIntent.putExtra("address_lat_long_dic",addressLatLong_Dictionary)
                context!!.startActivity(addAddressIntent)*/
            }
        })
    }

    private fun placeAPICallForAddress(searchString: String) {
        try {
            var requestBuilder =
                FindAutocompletePredictionsRequest.builder()
                    .setQuery(searchString)
                           .setCountry("AU")
                    .setTypeFilter(TypeFilter.ADDRESS)

            requestBuilder.setSessionToken(sessionToken)

            var task = placesClient?.findAutocompletePredictions(requestBuilder.build())
            task?.addOnSuccessListener(OnSuccessListener {

            stringify(it, false)

            })

            task?.addOnFailureListener(OnFailureListener {
                it.printStackTrace()
            })

            task?.addOnCompleteListener(OnCompleteListener {

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun stringify(response: FindAutocompletePredictionsResponse, raw: Boolean) {
        for (autocompletePrediction: AutocompletePrediction in response.getAutocompletePredictions()) {
               //loadData(Model_AddAddress_n_Fav(0, autocompletePrediction.getPrimaryText(null).toString(), autocompletePrediction.getFullText( null).toString(),autocompletePrediction.getSecondaryText( null).toString(),autocompletePrediction.distanceMeters))
        }

    }
    private fun loadData(modal_StopShareData: Model_AddAddress_n_Fav) {
        arrayPfAddress_N_Fav.add(modal_StopShareData)
        if (arrayPfAddress_N_Fav.size > 4) {
            //re?.adapter = MyPlaceAdapter(this, arrayPfAddress_N_Fav)
           //spinner?.performClick()
        }
    }

}