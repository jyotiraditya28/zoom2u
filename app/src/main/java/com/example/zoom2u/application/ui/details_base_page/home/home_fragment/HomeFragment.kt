package com.example.zoom2u.application.ui.details_base_page.home.home_fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.zoom2u.R
import com.example.zoom2u.databinding.FragmentHomeBinding
import com.example.zoom2u.application.ui.chat.ChatActivity
import com.example.zoom2u.application.ui.details_base_page.home.map_page.MapActivity


class HomeFragment : Fragment(), View.OnClickListener{


    lateinit var adapter : IconAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        if (container != null) {
            setAdpterView(binding, container.context)
        }

        binding.getQuoteBtn.setOnClickListener(this)
        binding.chatBtn.setOnClickListener(this)

        return binding.root
    }

    fun setAdpterView(binding: FragmentHomeBinding, context: Context) {
        var layoutManager = GridLayoutManager(activity, 2)
        binding.iconView.layoutManager = layoutManager
        (binding.iconView.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false
        adapter = IconAdapter(context, IconDataProvider.iconList.toList())

        binding.iconView.adapter = adapter



    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.get_quote_btn -> {
                var intent = Intent(activity, MapActivity::class.java)
                intent.putParcelableArrayListExtra("icon_data",ArrayList(IconDataProvider.iconList.toList()))
                startActivity(intent)
            }
            R.id.chat_btn -> {
                val intent = Intent(activity, ChatActivity::class.java)
                startActivity(intent)
            }

        }
    }



}