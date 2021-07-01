package com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.home.map_page

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.zoom2u_customer.R
import com.zoom2u_customer.ui.buttom_navigation_package.chat.ChatActivity
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.home.delivery_details.DeliveryDetailsActivity
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.home.home_fragment.Icon
import com.zoom2u_customer.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener,
    GoogleMap.OnMarkerClickListener {
    private lateinit var binding: ActivityMapBinding
    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var adapter: ItemMapDocCount
    private lateinit var dataList: ArrayList<Icon>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)

        var intent: Intent = intent
        dataList= intent.getParcelableArrayListExtra<Icon>("icon_data") as ArrayList<Icon>

        onItemClick()
        setAdpterView()


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    fun onItemClick() {
        binding.chatBtn.setOnClickListener(this)
        binding.deliveryDetailsBtn.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
    }

    fun setAdpterView() {
        var layoutManager = GridLayoutManager(this, 1)
        binding.iconView.layoutManager = layoutManager
        adapter = ItemMapDocCount(this,dataList)
        binding.iconView.adapter = adapter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        setUpMap()

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        map.isMyLocationEnabled = true


        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.delivery_details_btn -> {
                val intent = Intent(this, DeliveryDetailsActivity::class.java)
                startActivity(intent)
            }
            R.id.back_btn -> {
                finish()
            }
            R.id.chat_btn -> {
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
            }

        }
    }

    override fun onMarkerClick(p0: Marker?) = false

}