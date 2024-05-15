package com.example.myapplication.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Endpoint {

    var pais : String
    @GET("/open-data/distrits-islands.json")
    fun getCurrencies() : Call<JsonObject>

    @GET("/open-data/forecast/meteorology/cities/daily/{pais}.json")
    fun getCurrencyRate(@Path(value = "pais", encoded = true) pais: String): Call<JsonObject>

}