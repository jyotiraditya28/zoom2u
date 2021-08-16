package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.active_biddetails

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zoom2u_customer.databinding.FragmentBidDetailsBinding
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.ShowBidImageAdapter
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.BidDetailsResponse
import com.zoom2u_customer.utility.AppUtility


class BidDetailsFragment(var bidDetails: BidDetailsResponse?) : Fragment() {
    lateinit var binding: FragmentBidDetailsBinding
    var bidDetail: BidDetailsResponse? = null
    private var adapter: ShowBidImageAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBidDetailsBinding.inflate(inflater, container, false)
        this.bidDetail = bidDetails

        setDataToView(bidDetail)
        setAdapterView(container?.context)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToView(bidDetail: BidDetailsResponse?) {
        binding.pickAdd.text = bidDetail?.PickupLocation?.Street
        binding.dropAdd.text = bidDetail?.DropLocation?.Street

        val pickDateTime = AppUtility.getDateTimeFromDeviceToServerForDate(bidDetail?.PickupDateTime)
        val pickUpDateTimeSplit: Array<String>? = pickDateTime?.split(" ")?.toTypedArray()
        binding.pickTime.text = pickUpDateTimeSplit?.get(1) + " " + pickUpDateTimeSplit?.get(2) + " | " + pickUpDateTimeSplit?.get(0)

        val dropDateTime = AppUtility.getDateTimeFromDeviceToServerForDate(bidDetail?.DropDateTime)
        val dropUpDateTimeSplit: Array<String>? = dropDateTime?.split(" ")?.toTypedArray()
        binding.dropTime.text = dropUpDateTimeSplit?.get(1) + " " + dropUpDateTimeSplit?.get(2) + " | " + dropUpDateTimeSplit?.get(0)

        binding.offer.text=bidDetail?.Offers?.size.toString()
        binding.notes.text=bidDetail?.Notes
    }


    fun setAdapterView(context: Context?) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.bidImages.layoutManager = layoutManager
        adapter = ShowBidImageAdapter(context, bidDetail?.PackageImages!!,  onItemClick = ::onItemClick)
        binding.bidImages.adapter = adapter


    }
    fun onItemClick(imagePath:String){

    }
}