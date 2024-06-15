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
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

class MostraMetreologiaActivity : AppCompatActivity() {
    private lateinit var testSpinner : Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var bottomNavigationView: BottomNavigationView
    private  lateinit var sharedPreferences: SharedPreferences
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var estrelaColoridaImageView: ImageView
    private lateinit var favCustomAdapter: CustomAdapterTempo
    private lateinit var estrelaImageView: ImageView

    private var conta = 0
    var districtGlobalIds = mutableMapOf<String, Int>()
    var tempoInfomationn = ArrayList<TempoInformation>()
    var uvInformation = ArrayList<UvInformation>()
    var newData = HashMap<String, Any>()

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
        estrelaColoridaImageView = findViewById(R.id.estrelaColoridaImageView)
        estrelaImageView = findViewById(R.id.estrelaImageView)
        constraintLayout = findViewById(R.id.main)
        getCurrencies()

        sharedPreferences = getSharedPreferences("def", Context.MODE_PRIVATE)
        val checked = sharedPreferences.getBoolean("switch_checked", false)
        if (checked) {
            constraintLayout.setBackgroundColor(getColor(R.color.cinzaEscuro))
        } else {
            constraintLayout.setBackgroundColor(getColor(R.color.white))
        }




        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val dayAfterTomorrow = today.plusDays(2)

        val todayFormatted = today.format(dateFormatter)
        val tomorrowFormatted = tomorrow.format(dateFormatter)
        val dayAfterTomorrowFormatted = dayAfterTomorrow.format(dateFormatter)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fav -> {
                    val intent = Intent(this, FavoritoActivity::class.java)
                    startActivity(intent)
                    true
                }
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
                    sharedPreferences = getSharedPreferences("def", Context.MODE_PRIVATE)
                    sharedPreferences.edit().clear().apply()
                    sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE)
                    sharedPreferences.edit().clear().apply()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun removeItemFromDatabase(selectedDistrict: String) {
        val sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString("uidProfile", "")

        uid?.let {
            val databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Perfil").child(uid).child("locais")

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        val local = childSnapshot.child("local").getValue(String::class.java)
                        local?.let {
                            if (it == selectedDistrict) {
                                childSnapshot.ref.removeValue().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this@MostraMetreologiaActivity, "Local removido do banco de dados", Toast.LENGTH_SHORT).show()
                                        estrelaImageView.visibility = View.VISIBLE
                                        estrelaColoridaImageView.visibility = View.INVISIBLE
                                    } else {
                                        Toast.makeText(this@MostraMetreologiaActivity, "Falha ao remover local do banco de dados", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Tratamento de erro, se necessário
                }
            })
        }
    }



    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(temperaturaMaxima: String, temperaturamMinima: String) {
        val temperaturaMaximaa = temperaturaMaxima.toDouble().roundToInt().toString()
        val temperaturaMinimaa = temperaturamMinima.toDouble().roundToInt().toString()
        //val intent = Intent(applicationContext, Notification::class.java)
        val title = "Notificação de Temperatura"
        val message = "A Temp. Minima é de " + temperaturaMinimaa + "º \nTemp. Máxima é de " + temperaturaMaximaa + "º"
        /*intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)*/
        Toast.makeText(this, "Notificação agendada", Toast.LENGTH_SHORT).show()

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

        //showAlert(time, title, message)
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








                                estrelaColoridaImageView.setOnClickListener {
                                    removeItemFromDatabase(selectedDistrict)
                                }

                                estrelaImageView.setOnClickListener {
                                    sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE)
                                    val uid = sharedPreferences.getString("uidProfile", "")
                                    val databaseReference = FirebaseDatabase.getInstance().reference.child("Perfil").child(uid.toString()).child("locais")
                                    val newData = HashMap<String, Any>()
                                    newData["imagem"] = tempoInfomationn[0].tempoImage
                                    newData["local"] = selectedDistrict
                                    newData["tmax"] = tempoInfomationn[0].maxTemperatura
                                    newData["tmin"] = tempoInfomationn[0].minTemperatura

                                    // Find the next available ID
                                    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            var nextId = 1 // Start from 1 if no existing data
                                            for (childSnapshot in dataSnapshot.children) {
                                                val id = childSnapshot.key?.toIntOrNull()
                                                if (id != null && id >= nextId) {
                                                    nextId = id + 1
                                                }
                                            }

                                            // Set the new data with the next available ID
                                            databaseReference.child(nextId.toString()).setValue(newData)
                                            Toast.makeText(this@MostraMetreologiaActivity, "Local adicionado ao banco de dados", Toast.LENGTH_SHORT).show()
                                            estrelaImageView.visibility = View.INVISIBLE
                                            estrelaColoridaImageView.visibility = View.VISIBLE
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            // Handle errors here
                                        }
                                    })
                                }



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

    fun verFavorito() {
        val sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString("uidProfile", "")

        uid?.let {
            val databaseReference = FirebaseDatabase.getInstance().getReference("Perfil").child(uid).child("locais")

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favoritos = ArrayList<String>()

                    for (childSnapshot in snapshot.children) {
                        val local = childSnapshot.child("local").getValue(String::class.java)
                        local?.let { favoritos.add(it) }
                    }
                    verificarSelecao(favoritos)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Tratamento de erro, se necessário


                }
            })
        }
    }

    private fun verificarSelecao(favoritos: List<String>) {
        val selectedDistrict = testSpinner.selectedItem.toString()

        if (favoritos.contains(selectedDistrict)) {
            estrelaColoridaImageView.visibility = View.VISIBLE
            estrelaImageView.visibility = View.INVISIBLE

            if (estrelaColoridaImageView.visibility == View.VISIBLE) {
                val sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE)
                val uid = sharedPreferences.getString("uidProfile", "") ?: return
                val databaseReference = FirebaseDatabase.getInstance().reference.child("Perfil").child(uid).child("locais")

                newData = hashMapOf(
                    "imagem" to tempoInfomationn[0].tempoImage,
                    "local" to selectedDistrict,
                    "tmax" to tempoInfomationn[0].maxTemperatura,
                    "tmin" to tempoInfomationn[0].minTemperatura
                )

                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var entryFound = false

                        val genericTypeIndicator = object : GenericTypeIndicator<Map<String, Any>>() {}

                        for (childSnapshot in dataSnapshot.children) {
                            val childData = childSnapshot.getValue(genericTypeIndicator)
                            if (childData?.get("local") == selectedDistrict) {
                                // Update the existing entry
                                databaseReference.child(childSnapshot.key!!).updateChildren(newData)
                                    .addOnSuccessListener {
                                        Log.e("Selected", "Entry updated successfully")
                                    }
                                    .addOnFailureListener {
                                        Log.e("Selected", "Failed to update entry")
                                    }
                                entryFound = true
                                break
                            }
                        }

                        if (!entryFound) {
                            Log.e("Selected", "Entry not found")
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@MostraMetreologiaActivity, "Erro ao acessar o banco de dados", Toast.LENGTH_SHORT).show()
                    }
                })
            }


        } else {
            estrelaColoridaImageView.visibility = View.INVISIBLE
            estrelaImageView.visibility = View.VISIBLE
        }
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
                                verFavorito()
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

