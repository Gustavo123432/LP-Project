package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.api.Endpoint
import com.example.myapplication.definicoes.DefinicoesActivity
import com.example.myapplication.models.MarInformation
import com.example.myapplication.models.TempoInformation
import com.example.myapplication.util.NetworkUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class MarActivity : AppCompatActivity() {
    private lateinit var testSpinner : Spinner
    private lateinit var marRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var bottomNavigationView: BottomNavigationView
    private  lateinit var sharedPreferences: SharedPreferences
    private lateinit var constraintLayout: ConstraintLayout
    private var conta = 0
    private var contador = 0
    var districtGlobalIds = mutableMapOf<String, Int>()
    var tempoInfomationn = ArrayList<TempoInformation>()
    var marInformation = ArrayList<MarInformation>()
    var mar1Information = ArrayList<MarInformation>()
    var mar2Information = ArrayList<MarInformation>()

    private val totalTime = 500
    private val interval = 100
    private var elapsedTime = 0
    private var todayFormatted =""
    private var tomorrowFormatted=""
    private var dayAfterTomorrowFormatted=""

    var globalId = ""
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        testSpinner = findViewById(R.id.localMarSpinner)
        marRecyclerView = findViewById(R.id.marRecyclerView)
        bottomNavigationView = findViewById(R.id.bottomNavigationViewMar)
        progressBar = findViewById(R.id.progressBar3)
        constraintLayout = findViewById(R.id.main)

        getCurrencies()

        sharedPreferences = getSharedPreferences("def", Context.MODE_PRIVATE)
        val checked = sharedPreferences.getBoolean("switch_checked", false)
        if (checked) {
            constraintLayout.setBackgroundColor(getColor(R.color.cinzaEscuro))
        } else {
            constraintLayout.setBackgroundColor(getColor(R.color.white))
        }

        val dateFormatter = DateTimeFormatter.ofPattern("dd, EEEE", Locale("pt", "BR"))
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val dayAfterTomorrow = today.plusDays(2)

        todayFormatted = today.format(dateFormatter)
        tomorrowFormatted = tomorrow.format(dateFormatter)
        dayAfterTomorrowFormatted = dayAfterTomorrow.format(dateFormatter)


        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tempo -> {
                    val intent = Intent(this, MostraMetreologiaActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.mar -> {
                    val intent = Intent(this, MarActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settings -> {
                    val intent = Intent(this, DefinicoesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.logout -> {
                    sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit().clear().apply()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }



        fun getCurrencies (){
            val retrofitClient = NetworkUtils.getRetrofitInstance("https://api.ipma.pt/")
            val endpoint = retrofitClient.create(Endpoint::class.java)

            endpoint.getSeaLocation().enqueue(object : retrofit2.Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    if (response.isSuccessful) {
                        val data = mutableListOf<String>()
                        val body = response.body()

                        if (body != null) {
                            val jsonArray = body.getAsJsonArray()

                            jsonArray?.forEach { element ->
                                val jsonObject = element.asJsonObject
                                val districtName = jsonObject.get("local").asString
                                val globalId = jsonObject.get("globalIdLocal").asInt

                                // Preencha o mapa com os nomes dos distritos e seus IDs globais
                                districtGlobalIds[districtName] = globalId

                                data.add(districtName)
                            }
                            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            val distrito = sharedPreferences.getString("regiaoCusteira", null)
                            val position = sharedPreferences.getInt("posicaoRegiaoCusteira", 0)
                            val adapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, data)
                            testSpinner.adapter = adapter
                            if(distrito != null){
                                testSpinner.setSelection(position)
                            }



                            // Listener para quando um item é selecionado no spinner
                            testSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    // Recupere o nome do distrito selecionado
                                    val selectedDistrict = parent?.getItemAtPosition(position).toString()
                                    // Recupere o ID global correspondente do mapa
                                    globalId = districtGlobalIds[selectedDistrict].toString()
                                    sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putString("regiaoCusteira", selectedDistrict)
                                    editor.putInt("posicaoRegiaoCusteira", position)
                                    editor.apply()

                                    marInformation.clear()
                                    mar1Information.clear()
                                    mar2Information.clear()
                                    progressBar()
                                    waveDay0()
                                  /*  val customAdapterTempo = CustomAdapterMar(marInformation, mar1Information, mar2Information, globalId, this@MarActivity)
                                    marRecyclerView.layoutManager = LinearLayoutManager(this@MarActivity, LinearLayoutManager.HORIZONTAL, false)
                                    marRecyclerView.adapter = customAdapterTempo*/
                                    // Faça o que for necessário com o ID global
                                    // Por exemplo, armazene-o em uma variável ou passe-o para outra função
                                    Log.d("Selected District", "Name: $selectedDistrict, Global ID: $globalId")
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                }
                            }
                        } else {
                            Log.e("Response Error", "Response body is null")
                        }
                    } else {
                        Log.e("Response Error", "Unsuccessful response: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    print("não foi")

                }

            })
        }

       fun waveDay0() {

            val retrofitClient = NetworkUtils.getRetrofitInstance("https://api.ipma.pt/")
            val endpoint = retrofitClient.create(Endpoint::class.java)

            endpoint.getDay0Wave().enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val data = mutableListOf<String>()
                        val body = response.body()

                        if (body != null) {
                            val jsonArray = body.getAsJsonArray("data")

                            jsonArray?.forEach { element ->
                                val jsonObject = element.asJsonObject
                                val globalIdLocal = jsonObject.get("globalIdLocal").asString
                                val periodoMinimoOnda = jsonObject.get("wavePeriodMin").asString
                                val marTotalMaximo = jsonObject.get("totalSeaMax").asDouble
                                val ondulacaoMax = jsonObject.get("waveHighMax").asString
                                val ondulacaoMin = jsonObject.get("waveHighMin").asString
                                val periodoMaximoOnda = jsonObject.get("wavePeriodMax").asString
                                val temperaturaMaximaMar = jsonObject.get("sstMax").asString
                                val marTotalMinimo = jsonObject.get("totalSeaMin").asDouble
                                val direcaoOnda = jsonObject.get("predWaveDir").asString
                                val temperaturaMinimaMar = jsonObject.get("sstMin").asString
                                val dia = todayFormatted.toString()


                                val information = MarInformation(globalIdLocal,dia, periodoMinimoOnda, marTotalMaximo, ondulacaoMax, ondulacaoMin, periodoMaximoOnda, marTotalMinimo ,temperaturaMaximaMar, direcaoOnda, temperaturaMinimaMar)
                                // Preencha o mapa com os nomes dos distritos e seus IDs globais
                                marInformation.add(information)
                               }

                                waveDay1()
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

    fun waveDay1() {

        val retrofitClient = NetworkUtils.getRetrofitInstance("https://api.ipma.pt/")
        val endpoint = retrofitClient.create(Endpoint::class.java)

        endpoint.getDay1Wave().enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = mutableListOf<String>()
                    val body = response.body()

                    if (body != null) {
                        val jsonArray = body.getAsJsonArray("data")

                        jsonArray?.forEach { element ->
                            val jsonObject = element.asJsonObject
                            val globalIdLocal = jsonObject.get("globalIdLocal").asString
                            val periodoMinimoOnda = jsonObject.get("wavePeriodMin").asString
                            val marTotalMaximo = jsonObject.get("totalSeaMax").asDouble
                            val ondulacaoMax = jsonObject.get("waveHighMax").asString
                            val ondulacaoMin = jsonObject.get("waveHighMin").asString
                            val periodoMaximoOnda = jsonObject.get("wavePeriodMax").asString
                            val temperaturaMaximaMar = jsonObject.get("sstMax").asString
                            val marTotalMinimo = jsonObject.get("totalSeaMin").asDouble
                            val direcaoOnda = jsonObject.get("predWaveDir").asString
                            val temperaturaMinimaMar = jsonObject.get("sstMin").asString
                            val dia = tomorrowFormatted.toString()


                            val information = MarInformation(globalIdLocal,dia, periodoMinimoOnda, marTotalMaximo, ondulacaoMax, ondulacaoMin, periodoMaximoOnda, marTotalMinimo ,temperaturaMaximaMar, direcaoOnda, temperaturaMinimaMar)
                            // Preencha o mapa com os nomes dos distritos e seus IDs globais
                            mar1Information.add(information)
                            waveDay2()
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
    fun waveDay2() {

        val retrofitClient = NetworkUtils.getRetrofitInstance("https://api.ipma.pt/")
        val endpoint = retrofitClient.create(Endpoint::class.java)

        endpoint.getDay2Wave().enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = mutableListOf<String>()
                    val body = response.body()

                    if (body != null) {
                        val jsonArray = body.getAsJsonArray("data")

                        jsonArray?.forEach { element ->
                            val jsonObject = element.asJsonObject
                            val globalIdLocal = jsonObject.get("globalIdLocal").asString
                            val periodoMinimoOnda = jsonObject.get("wavePeriodMin").asString
                            val marTotalMaximo = jsonObject.get("totalSeaMax").asDouble
                            val ondulacaoMax = jsonObject.get("waveHighMax").asString
                            val ondulacaoMin = jsonObject.get("waveHighMin").asString
                            val periodoMaximoOnda = jsonObject.get("wavePeriodMax").asString
                            val temperaturaMaximaMar = jsonObject.get("sstMax").asString
                            val marTotalMinimo = jsonObject.get("totalSeaMin").asDouble
                            val direcaoOnda = jsonObject.get("predWaveDir").asString
                            val temperaturaMinimaMar = jsonObject.get("sstMin").asString
                            val dia = dayAfterTomorrowFormatted.toString()


                            val information = MarInformation(
                                globalIdLocal,
                                dia,
                                periodoMinimoOnda,
                                marTotalMaximo,
                                ondulacaoMax,
                                ondulacaoMin,
                                periodoMaximoOnda,
                                marTotalMinimo,
                                temperaturaMaximaMar,
                                direcaoOnda,
                                temperaturaMinimaMar
                            )
                            // Preencha o mapa com os nomes dos distritos e seus IDs globais
                            mar2Information.add(information)

                        }

                        sendRecyclerView()
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
    fun sendRecyclerView(){
        val customAdapterMar = CustomAdapterMar(marInformation,mar1Information, mar2Information, globalId, this@MarActivity)
        marRecyclerView.layoutManager = LinearLayoutManager(this@MarActivity, LinearLayoutManager.HORIZONTAL, false)
        marRecyclerView.adapter = customAdapterMar
    }

    fun progressBar(){

        val countDownTimer = object : CountDownTimer(totalTime.toLong(), interval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar.visibility = View.VISIBLE
                val progress = ((totalTime - millisUntilFinished).toFloat() / totalTime * 100).toInt()
                progressBar.progress = progress
            }

            override fun onFinish() {
                progressBar.visibility = View.INVISIBLE
                progressBar.progress = 100
            }
        }

        countDownTimer.start()
    }

}