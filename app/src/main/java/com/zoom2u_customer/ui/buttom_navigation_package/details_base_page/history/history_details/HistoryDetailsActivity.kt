package com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.history.history_details

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.history.HistoryResponse
import com.zoom2u_customer.databinding.ActivityHistoryDetailsBinding
import com.zoom2u_customer.utility.AppUtility

class HistoryDetailsActivity : AppCompatActivity() {
    private var arrayCourierPick: List<String>? = null
    private var arrayCourierDrop: List<String>? = null
    private lateinit var googleMap: GoogleMap
    var fragment: MapFragment? = null
    var response : HistoryDetailsResponse ? = null
    // private var mapFragment = supportFragmentManager
    private var historyItem: HistoryResponse? = null
    lateinit var viewModel: HistoryDetailsViewModel
    private var repository: HistoryDetailsRepository? = null
    lateinit var binding: ActivityHistoryDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("HistoryItem") != null)
            historyItem = intent.getParcelableExtra("HistoryItem")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_history_details)

        viewModel = ViewModelProviders.of(this).get(HistoryDetailsViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository = HistoryDetailsRepository(serviceApi, this)
        viewModel.repository = repository
        viewModel.setHistoryDetails(historyItem?.BookingRef)

        viewModel.getHistoryDetails()?.observe(this) {
            if (it != null) {
                AppUtility.progressBarDissMiss()
                setDataToView(it)
            }
        }
    }

    private fun setDataToView(it: HistoryDetailsResponse) {
        this.response = it
        arrayCourierPick = it.PickupLocation?.split(",")
        arrayCourierDrop = it.DropLocation?.split(",")

        binding.pickAdd.text = it.PickupAddress
        binding.pickName.text = it.PickupContactName
        binding.pickPhone.text = it.PickupPhone
        binding.pickEmail.text = it.PickupEmail


        binding.dropAdd.text = it.DropAddress
        binding.dropName.text = it.DropContactName
        binding.dropPhone.text = it.DropPhone
        binding.dropEmail.text = it.DropEmail
        binding.dropAdd.text = it.DropAddress


        if (!TextUtils.isEmpty(it.PackageNotes))
            binding.packageNote.text = it.PackageNotes
        else
            binding.packageNote.text = "Important Documents Please take care of those boxes."

    }

 /*   private fun initilizeMap() {
        if (googleMap == null) {
            fragment = fragmentManager.findFragmentById(R.id.map) as WorkaroundMapFragment
            googleMap = fragment.getMapAsync(this)

            //googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
            //googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true)
            // Enable / Disable my location button
            googleMap.getUiSettings().setMyLocationButtonEnabled(true)
            // Enable / Disable Compass icon
            googleMap.getUiSettings().setCompassEnabled(true)
            // Enable / Disable Rotate gesture
            googleMap.getUiSettings().setRotateGesturesEnabled(true)
            // Enable / Disable zooming functionality
            googleMap.getUiSettings().setZoomGesturesEnabled(true)
            try {
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            arrayCourierPick?.get(0)!!.toDouble(),
                            arrayCourierPick?.get(1)!!.toDouble()
                        ), 10F
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                googleMap.animateCamera(
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
                if (response?.Status == "Accepted" || response?.Status == "On Route to Pickup") {
                    val url: String = getDirectionsUrl(
                        LatLng(
                            response!!.Latitude.toDouble(),
                            jObjOfDeliveryHistoryDetail.getString("Longitude").toDouble()
                        ),
                        LatLng(
                            arrayCourierPick.get(0).toDouble(),
                            arrayCourierPick.get(1).toDouble()
                        )
                    )
                    com.customer.tabitems.completeview.CompletedDetails.DownloadTask().execute(url)
                    val url1: String = getDirectionsUrl(
                        LatLng(
                            arrayCourierPick.get(0).toDouble(),
                            arrayCourierPick.get(1).toDouble()
                        ),
                        LatLng(
                            arrayCourierDrop.get(0).toDouble(),
                            arrayCourierDrop.get(1).toDouble()
                        )
                    )
                    com.customer.tabitems.completeview.CompletedDetails.DownloadTask().execute(url1)
                } else if (jObjOfDeliveryHistoryDetail.getString("Status") == "Picked up" || jObjOfDeliveryHistoryDetail.getString(
                        "Status"
                    ) == "On Route to Dropoff" || jObjOfDeliveryHistoryDetail.getString("Status") == "Tried to deliver"
                ) {
                    val url2: String = getDirectionsUrl(
                        LatLng(
                            jObjOfDeliveryHistoryDetail.getString("Latitude").toDouble(),
                            jObjOfDeliveryHistoryDetail.getString("Longitude").toDouble()
                        ),
                        LatLng(
                            arrayCourierDrop.get(0).toDouble(),
                            arrayCourierDrop.get(1).toDouble()
                        )
                    )
                    com.customer.tabitems.completeview.CompletedDetails.DownloadTask().execute(url2)
                } else {
                    val url3: String = getDirectionsUrl(
                        LatLng(
                            arrayCourierPick.get(0).toDouble(),
                            arrayCourierPick.get(1).toDouble()
                        ),
                        LatLng(
                            arrayCourierDrop.get(0).toDouble(),
                            arrayCourierDrop.get(1).toDouble()
                        )
                    )
                    com.customer.tabitems.completeview.CompletedDetails.DownloadTask().execute(url3)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            if (googleMap == null) {
                Toast.makeText(
                    applicationContext,
                    "Sorry! unable to create maps",
                    Toast.LENGTH_SHORT
                ).show()
            }
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


    fun addMarkers() {
        try {
            if (response?.Status == "Accepted" || response?.Status == "On Route to Pickup"
            ) {
                if (response?.Vehicle == "Van") {
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                response?.Latitude?.toDouble(),
                               "Longitude").toDouble()
                            )
                        ).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.icontruck))
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierPick!![0].toDouble(),
                                arrayCourierPick!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.picklocation_icon))
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierDrop!![0].toDouble(),
                                arrayCourierDrop!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinish))
                    )
                } else if (jObjOfDeliveryHistoryDetail.getString("Vehicle") == "Bike") {
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                jObjOfDeliveryHistoryDetail.getString(
                                    "Latitude"
                                ).toDouble(),
                                jObjOfDeliveryHistoryDetail.getString("Longitude").toDouble()
                            )
                        ).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.iconbike))
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierPick!![0].toDouble(),
                                arrayCourierPick!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.picklocation_icon))
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierDrop!![0].toDouble(),
                                arrayCourierDrop!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinish))
                    )
                } else if (jObjOfDeliveryHistoryDetail.getString("Vehicle") == "Car") {
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                jObjOfDeliveryHistoryDetail.getString(
                                    "Latitude"
                                ).toDouble(),
                                jObjOfDeliveryHistoryDetail.getString("Longitude").toDouble()
                            )
                        ).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.iconcar))
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierPick!![0].toDouble(),
                                arrayCourierPick!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.picklocation_icon))
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierDrop!![0].toDouble(),
                                arrayCourierDrop!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinish))
                    )
                }
            } else if (jObjOfDeliveryHistoryDetail.getString("Status") == "Picked up" || jObjOfDeliveryHistoryDetail.getString(
                    "Status"
                ) == "On Route to Dropoff" || jObjOfDeliveryHistoryDetail.getString("Status") == "Tried to deliver"
            ) {
                if (jObjOfDeliveryHistoryDetail.getString("Vehicle") == "Van") {
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                jObjOfDeliveryHistoryDetail.getString(
                                    "Latitude"
                                ).toDouble(),
                                jObjOfDeliveryHistoryDetail.getString("Longitude").toDouble()
                            )
                        ).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.icontruck))
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierDrop!![0].toDouble(),
                                arrayCourierDrop!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinish))
                    )
                } else if (jObjOfDeliveryHistoryDetail.getString("Vehicle") == "Bike") {
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                jObjOfDeliveryHistoryDetail.getString(
                                    "Latitude"
                                ).toDouble(),
                                jObjOfDeliveryHistoryDetail.getString("Longitude").toDouble()
                            )
                        ).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.iconbike))
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierDrop!![0].toDouble(),
                                arrayCourierDrop!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinish))
                    )
                } else if (jObjOfDeliveryHistoryDetail.getString("Vehicle") == "Car") {
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                jObjOfDeliveryHistoryDetail.getString(
                                    "Latitude"
                                ).toDouble(),
                                jObjOfDeliveryHistoryDetail.getString("Longitude").toDouble()
                            )
                        ).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.iconcar))
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierDrop!![0].toDouble(),
                                arrayCourierDrop!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinish))
                    )
                }
            } else {
                if (jObjOfDeliveryHistoryDetail.getString("Vehicle") == "Van") {
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierPick!![0].toDouble(),
                                arrayCourierPick!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.picklocation_icon))
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierDrop!![0].toDouble(),
                                arrayCourierDrop!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinish))
                    )
                } else if (jObjOfDeliveryHistoryDetail.getString("Vehicle") == "Bike") {
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierPick!![0].toDouble(),
                                arrayCourierPick!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.picklocation_icon))
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierDrop!![0].toDouble(),
                                arrayCourierDrop!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinish))
                    )
                } else if (jObjOfDeliveryHistoryDetail.getString("Vehicle") == "Car") {
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierPick!![0].toDouble(),
                                arrayCourierPick!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.picklocation_icon))
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                arrayCourierDrop!![0].toDouble(),
                                arrayCourierDrop!![1].toDouble()
                            )
                        ).title("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinish))
                    )
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }*/
}