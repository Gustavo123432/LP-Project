package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class PaginaCarregamentoActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagina_carregamento)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        progressBar = findViewById(R.id.progressBar2)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        progressBar()
    }
    fun progressBar(){
        val totalTime = 3000
        val interval = 100

        val countDownTimer = object : CountDownTimer(totalTime.toLong(), interval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar.visibility = View.VISIBLE
                val progress = ((totalTime - millisUntilFinished).toFloat() / totalTime * 100).toInt()
                progressBar.progress = progress
            }

            override fun onFinish() {
                progressBar.progress = 100
                val savedEmail = sharedPreferences.getString("email", null)
                val savedPassword = sharedPreferences.getString("password", null)
                if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                    loginUser(savedEmail, savedPassword)
                }
                else{
                    val intent = Intent(this@PaginaCarregamentoActivity, MainActivity::class.java)
                    startActivity(intent)
                }

            }
        }

        countDownTimer.start()
    }

    private fun loginUser(email: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this, MostraMetreologiaActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}