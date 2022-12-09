package com.example.arctest.repository

import com.example.arctest.common.RetrofitManager
import com.example.arctest.model.Poster

class PosterRepository {

    suspend fun getPosters(): List<Poster> {
        return RetrofitManager.getService(PosterApi::class.java).getPosts()
    }
}