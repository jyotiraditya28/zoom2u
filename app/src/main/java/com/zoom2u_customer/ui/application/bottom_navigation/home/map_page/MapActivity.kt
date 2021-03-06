package com.zoom2u_customer.ui.application.bottom_navigation.home.map_page

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    var isQuotesRequest:Boolean?=false
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
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
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
                binding.nextBtn.isClickable=false
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.nextBtn.isClickable=true

                }, 1000)

              /**1case-check if total weight >100*/

                if (getTotalWeight() > 100) {
                    DialogActivity.logoutDialog(
                        this,
                        "Confirm!",
                        "Due to the nature of this delivery, it will be created as a quote request and sent out to drivers for bidding on, instead of being a fixed quote. Our drivers will start providing quotes for this delivery, which you can view and accept. Do you wish to continue?",
                        "Yes","No",
                        onCancelClick=::onNoClick,
                        onOkClick = ::onItemClick
                    )
                }
                /**2case-count check*/
                else if(!getCountCheck()){
                    DialogActivity.logoutDialog(
                        this,
                        "Confirm!",
                        "Due to the nature of this delivery, it will be created as a quote request and sent out to drivers for bidding on, instead of being a fixed quote. Our drivers will start providing quotes for this delivery, which you can view and accept. Do you wish to continue?",
                        "Yes","No",
                        onCancelClick=::onNoClick,
                        onOkClick = ::onItemClick
                    )
                }
               else if(isQuotesRequest==true){
                    DialogActivity.logoutDialog(
                        this,
                        "Confirm!",
                        "Due to the nature of this delivery, it will be created as a quote request and sent out to drivers for bidding on, instead of being a fixed quote. Our drivers will start providing quotes for this delivery, which you can view and accept. Do you wish to continue?",
                        "Yes","No",
                        onCancelClick=::onNoClick,
                        onOkClick = ::onItemClick
                    )
                }
                else {
                    val intent = Intent(this, DeliveryDetailsActivity::class.java)
                    intent.putParcelableArrayListExtra("IconList", dataList)
                    intent.putExtra("isQuotesRequest",isQuotesRequest)
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
        isQuotesRequest = true
        val intent = Intent(this, DeliveryDetailsActivity::class.java)
        intent.putParcelableArrayListExtra("IconList", dataList)
        intent.putExtra("isQuotesRequest",isQuotesRequest)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        binding.nextBtn.isClickable=true
    }
    private fun onCancelClick() {
        val intent = Intent()
        setResult(11, intent)
        finish()
    }

    private fun onOkClick() {

    }
    private fun onNoClick(){

    }
    override fun onMarkerClick(p0: Marker?) = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1) {
            val icon: Icon? = data?.getParcelableExtra<Icon>("Icon")
            isQuotesRequest= data?.getBooleanExtra("isQuotesRequest",false)
            adapter.updateItem(icon)

        }
    }


    private fun getCountCheck(): Boolean {
        for (item in dataList) {
            if(item.Value==10&&item.quantity>30){
                return false
            }else if(item.Value==11&&item.quantity>15){
                return false
            }else if(item.Value==12&&item.quantity>15){
                return false
            }else if(item.Value==13&&item.quantity>15){
                return false
            }else if(item.Value==14&&item.quantity>4){
                return false
            }
        }
        return true
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