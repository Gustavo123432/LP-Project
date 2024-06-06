package com.example.myapplication.api

import com.google.gson.JsonArray
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

    @GET("/open-data/forecast/meteorology/uv/uv.json")
    fun getUv() : Call<JsonArray>

    @GET("/open-data/sea-locations.json")
    fun getSeaLocation() : Call<JsonArray>

    @GET("/open-data/forecast/oceanography/daily/hp-daily-sea-forecast-day0.json")
    fun getDay0Wave(): Call<JsonObject>

    @GET("/open-data/forecast/oceanography/daily/hp-daily-sea-forecast-day1.json")
    fun getDay1Wave(): Call<JsonObject>

    @GET("/open-data/forecast/oceanography/daily/hp-daily-sea-forecast-day2.json")
    fun getDay2Wave(): Call<JsonObject>


}