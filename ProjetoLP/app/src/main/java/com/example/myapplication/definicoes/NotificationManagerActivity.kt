package com.example.myapplication.definicoes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R

class NotificationManagerActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var timePicker: TimePicker
    private lateinit var button: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var voltarImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification_manager)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val opcao : ArrayList<String>
        opcao = arrayListOf("Todos os Dias", "Mudar Hora")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcao)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner = findViewById(R.id.spinner)
        timePicker = findViewById(R.id.timePicker)
        button = findViewById(R.id.saveButton)
        spinner.adapter = adapter
        timePicker.setIs24HourView(true)
        voltarImageView = findViewById(R.id.voltarImageView)
        voltarImageView.setOnClickListener {
            val intent = Intent(this, DefinicoesActivity::class.java)
            startActivity(intent)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(spinner.selectedItem == "Todos os Dias"){
                    timePicker.visibility = View.VISIBLE
                    button.setOnClickListener {
                        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("diaSemana", "Todos os Dias")
                        editor.putInt("hour", timePicker.hour)
                        editor.putInt("minute", timePicker.minute)
                        editor.apply()
                        Toast.makeText(this@NotificationManagerActivity, "Alterações guardadas com sucesso!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@NotificationManagerActivity, DefinicoesActivity::class.java)
                        startActivity(intent)

                    }

                }else if (spinner.selectedItem == "Mudar Hora"){
                    timePicker.visibility = View.VISIBLE
                    button.setOnClickListener{
                        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("diaSemana", "Mudar Hora")
                        editor.putString("hora", timePicker.hour.toString())
                        editor.putString("minute", timePicker.minute.toString())
                        editor.apply()
                        Toast.makeText(this@NotificationManagerActivity, "Hora alterada com sucesso!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@NotificationManagerActivity, DefinicoesActivity::class.java)
                        startActivity(intent)
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


    }
}