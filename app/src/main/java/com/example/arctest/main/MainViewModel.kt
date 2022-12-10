package com.example.arctest.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arctest.common.SingleLiveEvent
import com.example.arctest.model.Poster
import com.example.arctest.repository.PosterRepository
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {
    private val _postersLiveData: MutableLiveData<List<Poster>> = MutableLiveData()
    val posterLiveData: LiveData<List<Poster>> = _postersLiveData
    private val _toastEvent: SingleLiveEvent<String> = SingleLiveEvent()
    val toastEvent: LiveData<String> = _toastEvent as LiveData<String>

    private val repository: PosterRepository by lazy { PosterRepository() }

    fun fetchPosters(){
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.d("MainViewModel", "response error: ${throwable.message}")
            _toastEvent.postValue(throwable.message)
        }) {
            withContext(Dispatchers.IO){
                val posters = repository.getPosters()
                _postersLiveData.postValue(posters)
            }
        }
    }
}