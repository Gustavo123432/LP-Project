package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var loginButton : Button
    private lateinit var registarTextVIew : TextView
    private lateinit var asteriscoEmailTextView : TextView
    private lateinit var asteriscoPasswordTextView : TextView
    private lateinit var esqueceuPwdTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainMar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registarTextVIew = findViewById(R.id.registarTextView)
        asteriscoEmailTextView = findViewById(R.id.asteriscoEmailTextView)
        asteriscoPasswordTextView = findViewById(R.id.asteriscoPasswordTextView)
        esqueceuPwdTextView = findViewById(R.id.esqPwdTextView)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val savedEmail = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("password", null)
        if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            loginUser(savedEmail, savedPassword)
        }

        esqueceuPwdTextView.setOnClickListener{
            val intent = Intent(this, EsqueceuPwdActivity::class.java)
            startActivity(intent)
        }

        registarTextVIew.setOnClickListener{
            val intent = Intent(this, RegistarActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            asteriscoEmailTextView.visibility = View.INVISIBLE
            asteriscoPasswordTextView.visibility = View.INVISIBLE


            if (email.isBlank()) {
                Toast.makeText(this, "Email Obrigatório", Toast.LENGTH_SHORT).show()
                asteriscoEmailTextView.visibility = View.VISIBLE
            }
            if(password.isBlank()){
                Toast.makeText(this, "Password Obrigatória", Toast.LENGTH_SHORT).show()
                asteriscoPasswordTextView.visibility = View.VISIBLE

            }else if(password.length < 6){
                Toast.makeText(this, "Password Incorreta", Toast.LENGTH_SHORT).show()
                asteriscoPasswordTextView.visibility = View.VISIBLE
            }
            if(!email.isBlank() && !password.isBlank() && password.length >=6){
                asteriscoEmailTextView.visibility = View.INVISIBLE
                asteriscoPasswordTextView.visibility = View.INVISIBLE
                loginUser(email, password)
            }
        }
    }
    private fun saveLogin(email: String, password: String) {
        // Save login credentials using SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    private fun loginUser(email: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                saveLogin(email, password)
                val intent = Intent(this, MostraMetreologiaActivity::class.java)
                startActivity(intent)
            } else {

                Toast.makeText(this, "Email ou Password Incorretos \n Tente Novamente!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}