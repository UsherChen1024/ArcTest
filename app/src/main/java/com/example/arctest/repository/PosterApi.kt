package com.example.arctest.repository

import com.example.arctest.model.Poster
import retrofit2.http.GET

interface PosterApi {

    @GET("blog/posts.json")
    suspend fun getPosts(): List<Poster>
}