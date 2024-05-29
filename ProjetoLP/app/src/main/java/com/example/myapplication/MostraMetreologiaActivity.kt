package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Constraints
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.api.Endpoint
import com.example.myapplication.definicoes.DefinicoesActivity
import com.example.myapplication.models.TempoInformation
import com.example.myapplication.models.UvInformation
import com.example.myapplication.notify.NOTIFICATION_CHANNEL_ID
import com.example.myapplication.notify.NOTIFICATION_ID
import com.example.myapplication.notify.Notification
import com.example.myapplication.notify.messageExtra
import com.example.myapplication.notify.titleExtra
import com.example.myapplication.util.NetworkUtils
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class MostraMetreologiaActivity : AppCompatActivity() {
    private lateinit var testSpinner : Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var bottomNavigationView: BottomNavigationView
    private  lateinit var sharedPreferences: SharedPreferences

    private var conta = 0
    var districtGlobalIds = mutableMapOf<String, Int>()
    var tempoInfomationn = ArrayList<TempoInformation>()
    var uvInformation = ArrayList<UvInformation>()

    private val totalTime = 500
    private val interval = 100
    private var elapsedTime = 0
    var globalId = ""

    @SuppressLint("NewApi")
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
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        getCurrencies()

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val dayAfterTomorrow = today.plusDays(2)

        val todayFormatted = today.format(dateFormatter)
        val tomorrowFormatted = tomorrow.format(dateFormatter)
        val dayAfterTomorrowFormatted = dayAfterTomorrow.format(dateFormatter)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    val intent = Intent(this, MostraMetreologiaActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.profileFragment -> {
                    /*val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)*/
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


    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(temperaturaMaxima: String, temperaturamMinima: String) {
        val temperaturaMaximaa = temperaturaMaxima.toDouble().roundToInt().toString()
        val temperaturaMinimaa = temperaturamMinima.toDouble().roundToInt().toString()
        val intent = Intent(applicationContext, Notification::class.java)
        val title = "Notificação de Temperatura"
        val message = "A Temp. Minima é de " + temperaturaMinimaa + "º \nTemp. Máxima é de " + temperaturaMaximaa + "º"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        // Agendar notificação repetitiva todos os dias às 7:30
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            time,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        showAlert(time, title, message)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage("Title: " + title
                    + "\nMessage: " + message
                    + "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun getTime(): Long {

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val dia = sharedPreferences.getString("diaSemana", null)
        val hour = sharedPreferences.getInt("hour", 7)
        val minute = sharedPreferences.getInt("minute", 30)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Se a hora atual for depois das 7:30 de hoje, agendar para amanhã
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return calendar.timeInMillis
    }

    @SuppressLint("NewApi")
    private fun createNotificationChannel() {
        val name = "Notification Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
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
                        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val distrito = sharedPreferences.getString("distrito", null)
                        val position = sharedPreferences.getInt("position", 0)
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
                                editor.putString("distrito", selectedDistrict)
                                editor.putInt("position", position)
                                editor.apply()

                                tempoInfomationn.clear()
                                /*val customAdapterTempo = CustomAdapterTempo(tempoInfomationn, this@MostraMetreologiaActivity)
                                recyclerView.layoutManager = LinearLayoutManager(this@MostraMetreologiaActivity, LinearLayoutManager.HORIZONTAL, false)
                                recyclerView.adapter = customAdapterTempo*/
                                progressBar()
                                weatherUpdate()
                                conta == 0
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

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                print("não foi")

            }

        })
    }

        fun weatherUpdate() {

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
                                val precipitacao = jsonObject.get("precipitaProb").asString
                                val temperaturamMinima = jsonObject.get("tMin").asString
                                val temperaturaMaxima = jsonObject.get("tMax").asString
                                val direcaoVento = jsonObject.get("predWindDir").asString
                                val tempoId = jsonObject.get("idWeatherType").asString
                                val dia = jsonObject.get("forecastDate").asString

                                val information = TempoInformation(dia, tempoId, temperaturamMinima, temperaturaMaxima,direcaoVento, precipitacao)
                                // Preencha o mapa com os nomes dos distritos e seus IDs globais
                                tempoInfomationn.add(information)
                                if(conta == 0){
                                    createNotificationChannel()
                                    scheduleNotification(temperaturaMaxima, temperaturamMinima)
                                    conta = 1
                                }
                                uvUpdate()
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

        fun uvUpdate(){
            val retrofitClient = NetworkUtils.getRetrofitInstance("https://api.ipma.pt/")
            val endpoint = retrofitClient.create(Endpoint::class.java)

            endpoint.getUv().enqueue(object : retrofit2.Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    if (response.isSuccessful) {
                        val data = mutableListOf<String>()
                        val body = response.body()

                        if (body != null) {
                            val jsonArray = body.getAsJsonArray()

                            jsonArray?.forEach { element ->
                                val jsonObject = element.asJsonObject
                                val globalIdLocal = jsonObject.get("globalIdLocal").asString
                                val iUv = jsonObject.get("iUv").asString
                                val data = jsonObject.get("data").asString

                                val information = UvInformation(globalIdLocal, iUv, data)
                                // Preencha o mapa com os nomes dos distritos e seus IDs globais
                                uvInformation.add(information)
                            }
                            val customAdapterTempo = CustomAdapterTempo(tempoInfomationn, uvInformation, globalId, this@MostraMetreologiaActivity)
                            recyclerView.layoutManager = LinearLayoutManager(this@MostraMetreologiaActivity, LinearLayoutManager.HORIZONTAL, false)
                            recyclerView.adapter = customAdapterTempo

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

private fun <E> ArrayList<E>.add(dia: String?, tempoId: String?, temperaturamMinima: String?, temperaturaMaxima: String?, direcaoVento: String?, precipitacao: String?) {

}

