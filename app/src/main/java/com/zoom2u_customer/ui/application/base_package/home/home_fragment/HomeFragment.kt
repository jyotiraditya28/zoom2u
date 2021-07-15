package com.zoom2u_customer.ui.application.base_package.home.home_fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zoom2u_customer.R
import com.zoom2u_customer.ui.application.chat.ChatActivity
import com.zoom2u_customer.ui.application.base_package.home.map_page.MapActivity
import com.zoom2u_customer.databinding.FragmentHomeBinding
import com.zoom2u_customer.utility.DialogActivity


class HomeFragment : Fragment(), View.OnClickListener{

    private var itemList:MutableList<Icon> = ArrayList()
    lateinit var adapter : IconAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {


        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        if (container != null) {
            setAdapterView(binding, container.context)
        }

        binding.getQuoteBtn.setOnClickListener(this)
        binding.chatBtn.setOnClickListener(this)

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
                setItemData()
                if (itemList.size > 0) {
                    val intent = Intent(activity, MapActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    intent.putParcelableArrayListExtra("icon_data", ArrayList(itemList.toList()))
                    startActivityForResult(intent,11)
                }else{
                    DialogActivity.alertDialogSingleButton(activity, "Oops!", "Please select the type of parcel you want to send.")
                }
            }
            R.id.chat_btn -> {
                val intent = Intent(activity, ChatActivity::class.java)
                startActivity(intent)
            }

        }
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
            val iconList: ArrayList<Icon> =  data?.getParcelableArrayListExtra<Icon>("IconList") as ArrayList<Icon>


            itemList.clear()

        }
    }



}