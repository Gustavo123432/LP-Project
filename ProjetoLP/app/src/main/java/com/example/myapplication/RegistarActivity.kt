package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Build
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.myapplication.models.UserData

class RegistarActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmarPasswordEditText: EditText
    private lateinit var asteriscoEmailTextView: TextView
    private lateinit var asteriscoPasswordTextView: TextView
    private lateinit var asteriscoConfirmarPasswordTextView: TextView
    private lateinit var registarButton : Button
    private lateinit var database: DatabaseReference
    private lateinit var nomeEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainMar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        nomeEditText = findViewById(R.id.nomeEditText)
        confirmarPasswordEditText = findViewById(R.id.confirmarPasswordEditText)
        asteriscoEmailTextView = findViewById(R.id.asteriscoEmailTextView)
        asteriscoPasswordTextView = findViewById(R.id.asteriscoPasswordTextView)
        asteriscoConfirmarPasswordTextView = findViewById(R.id.asteriscoConfirmarPasswordlTextView)
        registarButton = findViewById(R.id.registarButton)

        asteriscoEmailTextView.visibility = View.INVISIBLE
        asteriscoPasswordTextView.visibility = View.INVISIBLE
        asteriscoConfirmarPasswordTextView.visibility = View.INVISIBLE
        database = FirebaseDatabase.getInstance().reference


        registarButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmarPassword = confirmarPasswordEditText.text.toString()
            val userName = nomeEditText.text.toString()

            if(email.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty()){
                Toast.makeText(this, "Todos os Campos são OBRIGATÓRIOS", Toast.LENGTH_SHORT).show()
                validator(email, password, confirmarPassword)
            }
            else if(!password.equals(confirmarPassword)){
                Toast.makeText(this, "Passwords não coincidem", Toast.LENGTH_SHORT).show()
            }
            else if( password.length <= 7){
                Toast.makeText(this, "Minimo de caracteres para password são de 8 caracteres", Toast.LENGTH_SHORT).show()
                // Your existing code before the registration block
            } else {
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Get the registered user
                            val user = firebaseAuth.currentUser
                            user?.let {
                                // Get the user's UID
                                val uid = it.uid
                                val sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("uidProfile", uid)
                                editor.apply()
                                val userData = UserData(nomeUtilizador = userName)

                                // Store the data in Firebase Realtime Database
                                database.child("Perfil").child(uid).setValue(userData)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            Toast.makeText(this, "Utilizador Registado com Sucesso", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                        } else {
                                            Toast.makeText(this, "Failed to save user data. Please try again.", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                // Optionally, save the UID or use it as needed
                            }

                            Toast.makeText(this, "Utilizador Registado com Sucesso", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }

    }
    fun validator(email:String, password: String, confirmarPassword: String){
        if(email.isEmpty()){
            asteriscoEmailTextView.visibility = View.VISIBLE
        }else{
            asteriscoEmailTextView.visibility = View.INVISIBLE
        }
        if(password.isEmpty()){
            asteriscoPasswordTextView.visibility = View.VISIBLE
        }else{
            asteriscoPasswordTextView.visibility = View.INVISIBLE
        }
        if(confirmarPassword.isEmpty()){
            asteriscoConfirmarPasswordTextView.visibility = View.VISIBLE
        }else{
            asteriscoConfirmarPasswordTextView.visibility = View.INVISIBLE
        }

    }
}