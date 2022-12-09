package com.example.arctest.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arctest.model.Poster
import com.example.arctest.repository.PosterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val _postersLiveData: MutableLiveData<List<Poster>> = MutableLiveData()
    val posterLiveData: LiveData<List<Poster>> = _postersLiveData
    private val repository: PosterRepository by lazy { PosterRepository() }

    fun fetchPosters(){
        viewModelScope.launch(Dispatchers.IO) {
            val posters = repository.getPosters()
            _postersLiveData.postValue(posters)
        }
    }
}