package com.example.myapplication.definicoes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.FavoritoActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.MarActivity
import com.example.myapplication.MostraMetreologiaActivity
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DefinicoesActivity : AppCompatActivity() {

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var definicoesButton: Button
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var temaSwitch: Switch
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_definicoes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainMar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        definicoesButton = findViewById(R.id.definicoesButton)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        temaSwitch = findViewById(R.id.temaSwirch)
        constraintLayout = findViewById(R.id.mainMar)
        textView = findViewById(R.id.textView3)
        sharedPreferences = getSharedPreferences("def", Context.MODE_PRIVATE)
        val checked = sharedPreferences.getBoolean("switch_checked", false)
        temaSwitch.isChecked = checked
        if (temaSwitch.isChecked) {
            textView.setTextColor(getColor(R.color.cinzaClaro))
            constraintLayout.setBackgroundColor(getColor(R.color.cinzaEscuro))
        } else {
            textView.setTextColor(Color.GRAY)
            constraintLayout.setBackgroundColor(getColor(R.color.white))
        }


        definicoesButton.setOnClickListener {
            val intent = Intent(this, NotificationManagerActivity::class.java)
            startActivity(intent)
        }

        temaSwitch.setOnClickListener {
            // Salva o estado do switch no SharedPreferences
            val sharedPreferences = getSharedPreferences("def", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("switch_checked", temaSwitch.isChecked)
            editor.apply()

            // Define os fundos e textos com base no estado do switch
            if (temaSwitch.isChecked) {
                textView.setTextColor(getColor(R.color.cinzaClaro))
                constraintLayout.setBackgroundColor(getColor(R.color.cinzaEscuro))
            } else {
                textView.setTextColor(Color.GRAY)
                constraintLayout.setBackgroundColor(getColor(R.color.white))
            }
        }




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
}