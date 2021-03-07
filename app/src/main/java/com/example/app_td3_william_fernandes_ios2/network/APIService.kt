package com.example.app_td3_william_fernandes_ios2.network

import retrofit2.Call
import retrofit2.http.GET
import com.example.app_td3_william_fernandes_ios2.BanqueResponse

interface APIService {
    @GET("accounts") // endpoint
    fun getAccount(): Call<List<BanqueResponse>>
}

