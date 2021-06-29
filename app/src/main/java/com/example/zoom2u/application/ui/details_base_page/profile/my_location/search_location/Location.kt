package com.example.zoom2u.application.ui.details_base_page.profile.my_location.search_location

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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.zoom2u.R
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

class Location : AppCompatActivity() {


    var sessionToken: AutocompleteSessionToken? = null

    private var placesClient: PlacesClient? = null

    var arrayPfAddress_N_Fav: ArrayList<Model_AddAddress_n_Fav>? = null
    var mAdapter: Adapter_AddAddress_n_Fav? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)



        if (!Places.isInitialized()) {
            val apiKey = this!!.getString(R.string.google_api_key)
            Places.initialize(this!!.applicationContext, apiKey)
        }
        sessionToken = AutocompleteSessionToken.newInstance()
        placesClient = Places.createClient(this!!)

        var recycler_AddAddressView = findViewById<RecyclerView>(R.id.recycler_AddAddressView)
        var edtWhereTo_AddAddressView = findViewById<EditText>(R.id.edtWhereTo_AddAddressView)
        var backBtn_AddAddressView = findViewById<ImageView>(R.id.backBtn_AddAddressView)
        var imageview_cross = findViewById<ImageView>(R.id.imageview_cross)
        var layoutParent = findViewById<RelativeLayout>(R.id.layoutParent)
        layoutParent.visibility= View.INVISIBLE

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
                    recycler_AddAddressView!!.visibility = View.VISIBLE
                    arrayPfAddress_N_Fav?.clear()
                    placeAPICallForAddress(edtWhereTo_AddAddressView.text.toString())
                } else
                    recycler_AddAddressView!!.visibility = View.GONE
            }
        })
        var arrayPfAddress_N_Fav = ArrayList<Model_AddAddress_n_Fav>()

        mAdapter = Adapter_AddAddress_n_Fav(this!!, arrayPfAddress_N_Fav!!, object : Adapter_AddAddress_n_Fav.OnItemClickListener {
            override fun onItemClick(item: Model_AddAddress_n_Fav) {
               //Toast.makeText(this,"place Click",Toast.LENGTH_LONG)

             /*   val addAddressIntent = Intent(this, AddStopDetail_Activity::class.java)
                addAddressIntent.putExtra("item_add_stop", item)
                //  addAddressIntent.putExtra("address_lat_long_dic",addressLatLong_Dictionary)
                this!!.startActivity(addAddressIntent)*/
            }
        })
        edtWhereTo_AddAddressView.setOnClickListener {
            recycler_AddAddressView.adapter = mAdapter
        }




    }

    private fun placeAPICallForAddress(searchString: String) {
        try {
            var requestBuilder =
                FindAutocompletePredictionsRequest.builder()
                    .setQuery(searchString)
//                            .setCountry("AU")
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
            //        loadData(Model_AddAddress_n_Fav(0, autocompletePrediction.getPrimaryText(null).toString(), autocompletePrediction.getFullText(/* matchStyle */ null).toString()))
        }
        mAdapter?.notifyDataSetChanged()
    }

}