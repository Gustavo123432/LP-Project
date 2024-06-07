package com.example.myapplication.models

data class MarInformation(
    val globalIdLocal:String,
    val periodoMinimoOnda : String,
    val marTotalMaximo : Double,
    val ondulacaoMax : String,
    val ondulacaoMinima: String,
    val periodoMaximoOnda : String,
    val marTotalMinimo : Double,
    val temperaturaMaximaMar : String,
    val direcaoOnda : String,
    val temperaturaMinimaMar : String,
)
