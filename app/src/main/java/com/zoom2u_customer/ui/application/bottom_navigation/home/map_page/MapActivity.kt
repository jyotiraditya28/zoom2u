package com.zoom2u_customer.ui.application.bottom_navigation.home.map_page

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.DeliveryDetailsActivity
import com.zoom2u_customer.ui.application.bottom_navigation.home.home_fragment.Icon
import com.zoom2u_customer.databinding.ActivityMapBinding
import com.zoom2u_customer.ui.application.bottom_navigation.home.map_page.doc_dimension.DocDimensionActivity
import com.zoom2u_customer.utility.DialogActivity

class MapActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener,
    GoogleMap.OnMarkerClickListener {
    private lateinit var binding: ActivityMapBinding
    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var adapter: ItemMapDocCountAdapter
    private lateinit var dataList: ArrayList<Icon>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)

        val intent: Intent = intent
        dataList = intent.getParcelableArrayListExtra<Icon>("icon_data") as ArrayList<Icon>

        setAdapterView()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // binding.chatBtn.setOnClickListener(this)
        binding.nextBtn.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)


    }

    fun setAdapterView() {
        val layoutManager = GridLayoutManager(this, 1)
        binding.iconView.layoutManager = layoutManager
        adapter = ItemMapDocCountAdapter(this, dataList, onItemClick = ::onItemClick)
        binding.iconView.adapter = adapter
    }

    private fun onItemClick(icon: Icon) {
        val intent = Intent(this, DocDimensionActivity::class.java)
        intent.putExtra("Icon", icon)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivityForResult(intent, 1)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = false
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

    private fun getTotalWeight(): Double {
        var totalWeight = 0.0
        for (item in dataList) {
            totalWeight += item.weight*item.quantity
        }
        return totalWeight
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.next_btn -> {
                if (getTotalWeight() > 100) {
                    DialogActivity.alertDialogOkCallback(this, "Total weight : "+getTotalWeight()+"Kg", "Items weighing over 30kg each or 100kg in total will need to be placed through our Bid Request services. The same goes for items with measurements exceeding 200cm and multiple number of items exceeding the limit per booking. This is to maintain the safety and good health of our drivers.",
                        onItemClick = ::onItemClick)
                } else {
                    val intent = Intent(this, DeliveryDetailsActivity::class.java)
                    intent.putParcelableArrayListExtra("IconList", dataList)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
            R.id.back_btn -> {
                DialogActivity.logoutDialog(
                    this,
                    "Abandon this booking?",
                    "This booking's information will be lost if you cancel.",
                    "Continue", "Abandon",
                    onCancelClick = ::onCancelClick,
                    onOkClick = ::onOkClick
                )
            }
            /* R.id.chat_btn -> {
                 val intent = Intent(this, ChatActivity::class.java)
                 startActivity(intent)
             }*/

        }
    }
    private fun onItemClick() {
        val intent = Intent(this, DeliveryDetailsActivity::class.java)
        intent.putParcelableArrayListExtra("IconList", dataList)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
    private fun onCancelClick() {
        val intent = Intent()
        setResult(11, intent)
        finish()
    }

    private fun onOkClick() {

    }

    override fun onMarkerClick(p0: Marker?) = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1) {
            val icon: Icon? = data?.getParcelableExtra<Icon>("Icon")
            adapter.updateItem(icon)
        }
    }


    override fun onBackPressed() {
        DialogActivity.logoutDialog(
            this,
            "Abandon this booking?",
            "This booking's information will be lost if you cancel.",
            "Continue", "Abandon",
            onCancelClick = ::onCancelClick,
            onOkClick = ::onOkClick
        )
    }

}