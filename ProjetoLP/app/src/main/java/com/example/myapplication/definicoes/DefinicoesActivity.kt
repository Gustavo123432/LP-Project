package com.example.myapplication.definicoes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.MainActivity
import com.example.myapplication.MarActivity
import com.example.myapplication.MostraMetreologiaActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMostraMetreologiaBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DefinicoesActivity : AppCompatActivity() {

    private lateinit var definicoesButton: Button
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_definicoes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        definicoesButton = findViewById(R.id.definicoesButton)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        definicoesButton.setOnClickListener {
            val intent = Intent(this, NotificationManagerActivity::class.java)
            startActivity(intent)
        }

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
}