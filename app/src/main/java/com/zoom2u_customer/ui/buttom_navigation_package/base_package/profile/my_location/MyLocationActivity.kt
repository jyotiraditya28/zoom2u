package com.zoom2u_customer.ui.buttom_navigation_package.base_package.profile.my_location

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.profile.my_location.edit_add_location.EditAddLocationActivity
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.profile.my_location.model.MyLocationResAndEditLocationReq
import com.zoom2u_customer.databinding.ActivityMyLocationBinding
import com.zoom2u_customer.utility.AppUtility

import java.util.*

class MyLocationActivity : AppCompatActivity() , View.OnClickListener {
    lateinit var binding: ActivityMyLocationBinding
    lateinit var viewModel: MyLocationViewModel
    var adapter: MyLocationAdapter? = null
    private var repository: MyLocationRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_location)

        setAdpterView()

        viewModel = ViewModelProvider(this).get(MyLocationViewModel::class.java)

        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository = MyLocationRepository(serviceApi, this)
        viewModel.repository = repository
        viewModel.getMyLocation()

        binding.addNewCard.setOnClickListener(this)


        viewModel.getMySuccess()?.observe(this) {

            if (it != null) {
                AppUtility.progressBarDissMiss()
                if (it.isNotEmpty()) {
                    adapter?.updateRecords(it)
                    binding.noMyLocation.visibility = View.GONE
                } else
                    binding.noMyLocation.visibility = View.VISIBLE
            }

        }

    }

    private fun setAdpterView() {
        val layoutManager = GridLayoutManager(this, 1)
        binding.myLocationView.layoutManager = layoutManager
        adapter = MyLocationAdapter(this, Collections.emptyList(), onItemClick = ::onItemClick)
        binding.myLocationView.adapter = adapter

    }

    private fun onItemClick(myLocationResponse: MyLocationResAndEditLocationReq) {
        val intent = Intent(this, EditAddLocationActivity::class.java)
        intent.putExtra("EditAddLocation",true);
        intent.putExtra("EditLocation", myLocationResponse)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivityForResult(intent,3)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.add_new_card -> {
                val intent = Intent(this, EditAddLocationActivity::class.java)
                intent.putExtra("EditAddLocation", false);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivityForResult(intent,4)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === 3 || requestCode==4) {
            viewModel.getMyLocation()
        }
    }
}