package com.example.zoom2u.ui.buttom_base_page.home_page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.zoom2u.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        if (container != null) {
            setAdpterView(binding,container.context)
        }

        return binding.root

    }
    fun setAdpterView(binding: FragmentHomeBinding,context: Context) {
        var layoutManager = GridLayoutManager(activity, 2)
        binding.iconView.layoutManager = layoutManager
        var adapter = IconAdpter(context, IconDatprovider.iconList.toList())
        binding.iconView.adapter=adapter

    }


    }