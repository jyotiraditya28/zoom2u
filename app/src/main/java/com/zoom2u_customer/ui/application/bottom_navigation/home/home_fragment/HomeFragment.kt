package com.zoom2u_customer.ui.application.bottom_navigation.home.home_fragment


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.FragmentHomeBinding
import com.zoom2u_customer.ui.application.bottom_navigation.home.map_page.MapActivity
import com.zoom2u_customer.utility.AppPreference
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity


class HomeFragment : Fragment(), View.OnClickListener{
    lateinit var binding : FragmentHomeBinding
    private var itemList:MutableList<Icon> = ArrayList()
    lateinit var adapter : IconAdapter
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
         binding = FragmentHomeBinding.inflate(inflater, container, false)


        if (container != null) {
            setAdapterView(binding, container.context)
        }
        binding.getQuoteBtn.setOnClickListener(this)
        //binding.chatBtn.setOnClickListener(this)

       if (!AppPreference.getSharedPrefInstance().getProfileData()?.FirstName.isNullOrBlank())
        binding.nameHeader.text= "Hi "+AppUtility.upperCaseFirst(AppPreference.getSharedPrefInstance().getProfileData()?.FirstName.toString())
        else
           binding.nameHeader.text= "Hi "+AppUtility.upperCaseFirst(AppPreference.getSharedPrefInstance().getLoginResponse()?.firstName.toString())

        return binding.root
    }






    fun setAdapterView(binding: FragmentHomeBinding, context: Context) {
        val layoutManager = GridLayoutManager(activity, 2)
        binding.iconView.layoutManager = layoutManager
        (binding.iconView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        adapter = IconAdapter(context, IconDataProvider.iconList)
        binding.iconView.adapter = adapter


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.get_quote_btn -> {
                binding.getQuoteBtn.isClickable=false
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.getQuoteBtn.isClickable=true

                }, 1000)
                setItemData()
                if (itemList.size > 0) {
                    val intent = Intent(activity, MapActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.putParcelableArrayListExtra("icon_data", ArrayList(itemList.toList()))
                    startActivityForResult(intent,11)
                }else{
                    DialogActivity.alertDialogOkCallbackWithoutHeader(activity,  "You’ll need to select what you’re trying to send first!" +
                            "\n" +
                            "Please select the type of parcels you want to send and we’ll sort out the rest.",onItemClick = ::onItemClick)

                }

                }
           /* R.id.chat_btn -> {
                val intent = Intent(activity, ChatActivity::class.java)
                startActivity(intent)
            }*/

        }
    }

    private fun onItemClick() {
        binding.getQuoteBtn.isClickable=true
    }

    private fun setItemData() {
        for (item in IconDataProvider.iconList) {
            if (item.quantity > 0)
                itemList.add(item)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 11) {
           setDefaultData()
        }
    }

    private fun setDefaultData() {
        for (item in IconDataProvider.iconList) {
            item.quantity=0
            adapter.updateItem(item)
        }
        itemList.clear()
    }

    override fun onResume() {
        super.onResume()
        setDefaultData()
    }


}