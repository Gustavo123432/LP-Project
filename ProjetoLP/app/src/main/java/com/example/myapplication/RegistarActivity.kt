package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistarActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmarPasswordEditText: EditText
    private lateinit var asteriscoEmailTextView: TextView
    private lateinit var asteriscoPasswordTextView: TextView
    private lateinit var asteriscoConfirmarPasswordTextView: TextView
    private lateinit var registarButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmarPasswordEditText = findViewById(R.id.confirmarPasswordEditText)
        asteriscoEmailTextView = findViewById(R.id.asteriscoEmailTextView)
        asteriscoPasswordTextView = findViewById(R.id.asteriscoPasswordTextView)
        asteriscoConfirmarPasswordTextView = findViewById(R.id.asteriscoConfirmarPasswordlTextView)
        registarButton = findViewById(R.id.registarButton)

        asteriscoEmailTextView.visibility = View.INVISIBLE
        asteriscoPasswordTextView.visibility = View.INVISIBLE
        asteriscoConfirmarPasswordTextView.visibility = View.INVISIBLE

        registarButton.setOnClickListener {

        }

    }
}