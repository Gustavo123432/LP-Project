package com.example.myapplication.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.ipma.pt/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: Endpoint by lazy {
        retrofit.create(Endpoint::class.java)
    }

}