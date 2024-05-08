package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.api.Endpoint
import com.example.myapplication.util.NetworkUtils
import com.google.gson.JsonObject
import pt.epvc.myapplication.adapters.CustomAdapterTempo
import retrofit2.Call
import retrofit2.Response

class MostraMetreologiaActivity : AppCompatActivity() {
    private lateinit var testSpinner : Spinner
    private lateinit var recyclerView: RecyclerView
    val districtGlobalIds = mutableMapOf<String, Int>()
    var globalId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mostra_metreologia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        testSpinner = findViewById(R.id.spinnerTest)
        recyclerView = findViewById(R.id.recyclerView)
        getCurrencies()
        weatherUpdate()

    }
    fun getCurrencies (){
        val retrofitClient = NetworkUtils.getRetrofitInstance("https://api.ipma.pt/")
        val endpoint = retrofitClient.create(Endpoint::class.java)

        endpoint.getCurrencies().enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = mutableListOf<String>()
                    val body = response.body()

                    if (body != null) {
                        val jsonArray = body.getAsJsonArray("data")

                        jsonArray?.forEach { element ->
                            val jsonObject = element.asJsonObject
                            val districtName = jsonObject.get("local").asString
                            val globalId = jsonObject.get("globalIdLocal").asInt

                            // Preencha o mapa com os nomes dos distritos e seus IDs globais
                            districtGlobalIds[districtName] = globalId

                            data.add(districtName)
                        }

                        val adapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, data)
                        testSpinner.adapter = adapter

                        // Listener para quando um item é selecionado no spinner
                        testSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                // Recupere o nome do distrito selecionado
                                val selectedDistrict = parent?.getItemAtPosition(position).toString()
                                // Recupere o ID global correspondente do mapa
                                globalId = districtGlobalIds[selectedDistrict].toString()
                                // Faça o que for necessário com o ID global
                                // Por exemplo, armazene-o em uma variável ou passe-o para outra função
                                Log.d("Selected District", "Name: $selectedDistrict, Global ID: $globalId")
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Não faz nada quando nada é selecionado
                            }
                        }
                    } else {
                        Log.e("Response Error", "Response body is null")
                    }
                } else {
                    Log.e("Response Error", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                print("não foi")

            }

        })
    }

    fun weatherUpdate(){
        val retrofitClient = NetworkUtils.getRetrofitInstance("https://api.ipma.pt/")
        val endpoint = retrofitClient.create(Endpoint::class.java)

        endpoint.getCurrencyRate(globalId).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = mutableListOf<String>()
                    val body = response.body()

                    if (body != null) {
                        val jsonArray = body.getAsJsonArray("data")

                        jsonArray?.forEach { element ->
                            val jsonObject = element.asJsonObject
                            val dataDePrevisao = jsonObject.get("forecastDate").asString
                            val temperaturaMinima = jsonObject.get("tMin").asString
                            val temperaturaMaxima = jsonObject.get("tMax").asString
                            val itensidadeVento = jsonObject.get("classWindSpeed").asString
                            val direcaoVento = jsonObject.get("predWindDir").asString
                            val probabilidadePrecipitacao = jsonObject.get("probPrecipita").asString
                            val idTempo = jsonObject.get("idWeatherType").asString

                            // Preencha o mapa com os nomes dos distritos e seus IDs globais
                            data.add(dataDePrevisao)
                            data.add(temperaturaMinima)
                            data.add(temperaturaMaxima)
                            data.add(itensidadeVento)
                            data.add(direcaoVento)
                            data.add(probabilidadePrecipitacao)
                            data.add(idTempo)
                        }

                        val adapter = CustomAdapterTempo(data, this@MostraMetreologiaActivity)

                        recyclerView.adapter = adapter

                        // Listener para quando um item é selecionado no spinner

                    } else {
                        Log.e("Response Error", "Response body is null")
                    }
                } else {
                    Log.e("Response Error", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                print("não foi")

            }

        })
    }
}