package com.example.zoom2u.application.ui.details_base_page.profile.my_location

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.zoom2u.R
import com.example.zoom2u.databinding.ActivityMyLocationBinding
import com.example.zoom2u.application.ui.details_base_page.history.history_details.HistoryDetailsActivity

class MyLocationActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyLocationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_location)

        setAdpterView()
    }

    private fun setAdpterView() {
        var layoutManager = GridLayoutManager(this, 1)
        binding.myLocationView.layoutManager = layoutManager
        var adapter = MyLocationAdapter(this, MyLocationProvider.myLocationList.toList(),onItemClick = ::onItemClick)
        binding.myLocationView.adapter = adapter

    }
    private fun onItemClick(myLocation: MyLocation){
        val intent = Intent(this, HistoryDetailsActivity::class.java)
        startActivity(intent)
    }
}