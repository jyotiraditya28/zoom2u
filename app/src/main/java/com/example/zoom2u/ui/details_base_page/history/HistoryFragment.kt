package com.example.zoom2u.ui.details_base_page.history

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.zoom2u.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    lateinit var binding: FragmentHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        if (container != null) {
            setAdpterView(binding,container.context)
        }

        return binding.root

    }
    fun setAdpterView(binding: FragmentHistoryBinding,context: Context) {
        var layoutManager = GridLayoutManager(activity, 1)
        binding.deliveryHistoryRecycler.layoutManager = layoutManager
        var adapter = HistoryItemAdpter(context, HistoryItemProvider.historyItem.toList())
        binding.deliveryHistoryRecycler.adapter=adapter

    }
}