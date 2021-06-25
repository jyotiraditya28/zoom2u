package com.example.zoom2u.application.ui.details_base_page.profile.my_location

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.zoom2u.R
import com.example.zoom2u.apiclient.ApiClient
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.databinding.ActivityMyLocationBinding
import com.example.zoom2u.application.ui.details_base_page.profile.my_location.edit_add_location.EditAddLocationActivity
import com.example.zoom2u.application.ui.details_base_page.profile.my_location.model.MyLocationResponse
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

        viewModel = ViewModelProviders.of(this).get(MyLocationViewModel::class.java)

        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = MyLocationRepository(serviceApi, this)
        viewModel.repository = repository
        viewModel.getMyLocation()

        binding.addNewCard.setOnClickListener(this)


        viewModel.getMySuccess()?.observe(this) {

            if (!it.isNullOrEmpty())
                adapter?.updateRecords(it)
        }

    }


    private fun setAdpterView() {
        val layoutManager = GridLayoutManager(this, 1)
        binding.myLocationView.layoutManager = layoutManager
        adapter = MyLocationAdapter(this, Collections.emptyList(), onItemClick = ::onItemClick)
        binding.myLocationView.adapter = adapter

    }

    private fun onItemClick(myLocationResponse: MyLocationResponse) {
        val intent = Intent(this, EditAddLocationActivity::class.java)
        intent.putExtra("EditAddLocation",true);
        intent.putExtra("EditLocation", myLocationResponse)
        startActivity(intent)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.add_new_card -> {
                val intent = Intent(this, EditAddLocationActivity::class.java)
                intent.putExtra("EditAddLocation", false);
                startActivity(intent)

            }
        }
    }
}