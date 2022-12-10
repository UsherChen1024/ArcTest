package com.example.arctest.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log

fun isOnline(context: Context): Boolean {
    val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
    val result = networkInfo?.isConnected == true
    Log.d("NetUtil", "isOnline = $result")
    return result
}