package com.example.zoom2u.application.ui.details_base_page.profile.my_location.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.zoom2u.application.ui.details_base_page.profile.my_location.model.MyLocationResAndEditLocationReq

@Dao
interface MyLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMyLocationList(myLocationResponse: List<MyLocationResAndEditLocationReq>)

    @Query("select * from MyLocationResponse")
    fun getMyLocationList(): LiveData<MutableList<MyLocationResAndEditLocationReq>>
}