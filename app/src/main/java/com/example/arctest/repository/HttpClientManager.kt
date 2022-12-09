package com.example.arctest.repository

import okhttp3.OkHttpClient

class HttpClientManager {
    companion object{
        val client by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            OkHttpClient.Builder().build()
        }
    }
}