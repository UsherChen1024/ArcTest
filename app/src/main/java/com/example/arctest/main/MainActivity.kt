package com.example.arctest.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.arctest.R
import com.example.arctest.common.isOnline
import com.example.arctest.databinding.ActivityMainBinding
import com.example.arctest.model.Poster

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private val posterList: MutableList<Poster> = mutableListOf()
    private lateinit var adapter: PosterListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        observeData()
        fetchData()
    }

    private fun fetchData() {
        if (isOnline(this)) {
            mainViewModel.fetchPosters()
        }else{
            Toast.makeText(this, "网络连接不可用", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeData() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.posterLiveData.observe(this) {
            posterList.clear()
            posterList.addAll(it)
            adapter.notifyDataSetChanged()
        }

        mainViewModel.toastEvent.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = PosterListAdapter(this, posterList)
        binding.recyclerView.adapter = adapter
    }
}