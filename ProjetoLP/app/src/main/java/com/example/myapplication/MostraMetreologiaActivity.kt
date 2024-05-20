package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.api.Endpoint
import com.example.myapplication.models.TempoInformation
import com.example.myapplication.util.NetworkUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import kotlin.math.log

class MostraMetreologiaActivity : AppCompatActivity(), CustomAdapterTempo.RecyclerViewEvent {
    //add only to click CustomAdapterTempo.RecyclerViewEvent
    private lateinit var testSpinner : Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var floatingActionButton: FloatingActionButton

    var districtGlobalIds = mutableMapOf<String, Int>()
    var tempoInfomationn = ArrayList<TempoInformation>()

    private val totalTime = 500
    private val interval = 100
    private var elapsedTime = 0

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
        progressBar = findViewById(R.id.progressBar)
        floatingActionButton = findViewById(R.id.floatingActionButton)
        getCurrencies(this)

        /*aparecer widgets com os 3 locais e o tempo previsto para esse dia, salvar o ultimo local escolhido shared preferences
        * logout sai da conta fazer o registar activity*/

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, test::class.java)
            startActivity(intent)
        }

    }
    fun getCurrencies (context: Context){
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

                                tempoInfomationn.clear()
                                val customAdapterTempo = CustomAdapterTempo(tempoInfomationn,this@MostraMetreologiaActivity)
                                recyclerView.layoutManager = LinearLayoutManager(this@MostraMetreologiaActivity, LinearLayoutManager.HORIZONTAL, false)
                                recyclerView.adapter = customAdapterTempo
                                progressBar()
                                weatherUpdate(this@MostraMetreologiaActivity)
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

        fun weatherUpdate(context: Context) {

            val retrofitClient = NetworkUtils.getRetrofitInstance("https://api.ipma.pt/")
            val endpoint = retrofitClient.create(Endpoint::class.java)

            endpoint.getCurrencyRate(globalId).enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val data = mutableListOf<String>()
                        val body = response.body()


                        // criar model to get the data
                        if (body != null) {
                            val jsonArray = body.getAsJsonArray("data")

                            jsonArray?.forEach { element ->
                                val jsonObject = element.asJsonObject
                                val precipitacao = jsonObject.get("precipitaProb").asString
                                val temperaturamMinima = jsonObject.get("tMin").asString
                                val temperaturaMaxima = jsonObject.get("tMax").asString
                                val direcaoVento = jsonObject.get("predWindDir").asString
                                val tempoId = jsonObject.get("idWeatherType").asString
                                val dia = jsonObject.get("forecastDate").asString

                                val information = TempoInformation(dia, tempoId, temperaturamMinima, temperaturaMaxima,direcaoVento, precipitacao)
                                // Preencha o mapa com os nomes dos distritos e seus IDs globais
                                tempoInfomationn.add(information)

                            }
                            //get cod recycler view
                            val customAdapterTempo = CustomAdapterTempo(tempoInfomationn, this@MostraMetreologiaActivity)
                            recyclerView.layoutManager = LinearLayoutManager(this@MostraMetreologiaActivity, LinearLayoutManager.HORIZONTAL, false)
                            recyclerView.adapter = customAdapterTempo
                           // val adapter = ArrayAdapter(baseContext, CustomAdapterTempo(tempoInfomationn), tempoInfomationn)
                            //testSpinner.adapter = adapter


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

    /*fun loadAndShowImage() {
    progressBar.visibility = View.VISIBLE
    Picasso.get()
        .load("sua_url_da_imagem")
        .into(imageView, object : Callback {
            override fun onSuccess() {
                progressBar.visibility = View.INVISIBLE
            }

            override fun onError(e: Exception?) {
            }
        })
}
*/

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
    override fun onItemClick(position: Int) {
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("lala", tempoInfomationn[position].dia)
        editor.apply()
        Log.e("Your Tag for identy", tempoInfomationn[position].dia);
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)


    }



}


private fun <E> ArrayList<E>.add(dia: String?, tempoId: String?, temperaturamMinima: String?, temperaturaMaxima: String?, direcaoVento: String?, precipitacao: String?) {

}

