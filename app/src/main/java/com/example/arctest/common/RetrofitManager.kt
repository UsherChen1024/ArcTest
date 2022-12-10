package com.example.arctest.common

import android.util.Log
import com.example.arctest.MainApp
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.cache.DiskLruCache
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object RetrofitManager {


    private var retrofit: Retrofit

    init {

        val logInterceptor = HttpLoggingInterceptor {
            Log.d("OkHttp:  ", it)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)

        val file = File(MainApp.instance.cacheDir, "okhttp_cache")
        val cache = Cache(file, 10 * 1024 * 1024)


        val okHttpClient = OkHttpClient().newBuilder()
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .cache(cache)
            .addInterceptor(logInterceptor)
            .addNetworkInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    Log.d("NetworkInterceptor", "real request url: ${chain.request().url}")
                    return chain.proceed(chain.request())
                }
            })
            .build()

        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    fun <T> getService(service: Class<T>): T {
        return retrofit.create(service)
    }
}