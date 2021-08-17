package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.GetAddressFromGoogle.GoogleAddressRepository
import com.zoom2u_customer.apiclient.GetAddressFromGoogleAPI
import com.zoom2u_customer.apiclient.GoogleServiceApi
import com.zoom2u_customer.apiclient.ServiceApi

import com.zoom2u_customer.databinding.ActivityActiveBidBinding
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.RouteParser
import org.json.JSONException

class ActiveBidActivity : AppCompatActivity(), OnMapReadyCallback {
    private var arrayCourierPick: List<String>? = null
    private var arrayCourierDrop: List<String>? = null
    lateinit var binding: ActivityActiveBidBinding
    private var quoteID: Int? = null
    private lateinit var map: GoogleMap
    private lateinit var viewpageradapter: BidViewPagerAdapter
    lateinit var viewModel: BidDetailsViewModel
    private var repositoryGoogleAddress: GoogleAddressRepository? = null
    private var repository: BidDetailsRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_bid)

        if (intent.hasExtra("QuoteId")) {
            quoteID = intent.getStringExtra("QuoteId")?.toInt()
        }

        viewModel = ViewModelProvider(this).get(BidDetailsViewModel::class.java)
        val googleServiceApi: GoogleServiceApi = GetAddressFromGoogleAPI.getGoogleServices()
        val serviceApi: ServiceApi = ApiClient.getServices()
        repositoryGoogleAddress = GoogleAddressRepository(googleServiceApi, this)
        repository = BidDetailsRepository(serviceApi, this)
        viewModel.repository = repository
        viewModel.repositoryGoogleAddress = repositoryGoogleAddress

        viewModel.getBidDetails(quoteID)

        val fragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        fragment.getMapAsync(this)

        viewModel.getBidDetailsSuccess()?.observe(this) {
            if (it != null) {
                AppUtility.progressBarDissMiss()
                initializeMap()

                viewpageradapter = BidViewPagerAdapter(supportFragmentManager, it)
                binding.pager.adapter = viewpageradapter
                binding.tabLayout.setupWithViewPager(binding.pager)
            }
        }

        viewModel.getRouteSuccess()?.observe(this) {
            if (!it.isNullOrEmpty())
               RouteParser.parserTask(this,map,it)
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        //hide zoom in out button in map
        map.uiSettings.isZoomControlsEnabled = false
        //googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        //googleMap.setMyLocationEnabled(true);
        // map.uiSettings.isZoomControlsEnabled = true
        // Enable / Disable my location button
        map.uiSettings.isMyLocationButtonEnabled = true
        // Enable / Disable Compass icon
        map.uiSettings.isCompassEnabled = true
        // Enable / Disable Rotate gesture
        map.uiSettings.isRotateGesturesEnabled = true
        // Enable / Disable zooming functionality
        map.uiSettings.isZoomGesturesEnabled = true
    }

    private fun initializeMap() {

        try {
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        arrayCourierPick?.get(0)!!.toDouble(),
                        arrayCourierPick?.get(1)!!.toDouble()
                    ), 10F
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        -33.8619486,
                        151.00586
                    ), 12F
                )
            )
        }
        addMarkers()
        try {
            // Getting URL to the Google Directions API

            val url3: String? = getDirectionsUrl(
                LatLng(
                    arrayCourierPick?.get(0)!!.toDouble(),
                    arrayCourierPick?.get(1)!!.toDouble()
                ),
                LatLng(
                    arrayCourierDrop?.get(0)!!.toDouble(),
                    arrayCourierDrop?.get(1)!!.toDouble()
                )
            )
            viewModel.getRoute(url3)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String? {
        // Origin of route
        val str_origin =
            "origin=" + origin.latitude.toString() + "," + origin.longitude

        // Destination of route
        val str_dest =
            "destination=" + dest.latitude.toString() + "," + dest.longitude

        // Sensor enabled
        val sensor =
            "sensor=false&key=" + "AIzaSyDXy3Z6OzAQ3siNfARS3Y54-sbhNQSBL0U" // Purchased account zoom.2ua@gmail.com


        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor"

        // Output format
        val output = "json"

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }

    private fun addMarkers() {
        try {

            map.addMarker(
                MarkerOptions().position(
                    LatLng(
                        arrayCourierPick!![0].toDouble(),
                        arrayCourierPick!![1].toDouble()
                    )
                ).title("").icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup_icon)
                )
            )
            map.addMarker(
                MarkerOptions().position(
                    LatLng(
                        arrayCourierDrop!![0].toDouble(),
                        arrayCourierDrop!![1].toDouble()
                    )
                ).title("").icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_drop_off_icon)
                )
            )


        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}