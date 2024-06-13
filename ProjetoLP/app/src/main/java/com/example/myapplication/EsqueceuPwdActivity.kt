package com.example.myapplication

import android.content.Intent
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

class EsqueceuPwdActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var asteriscoTextView : TextView
    private lateinit var recuperarButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_esqueceu_pwd)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainMar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailEditText = findViewById(R.id.emailEditText)
        recuperarButton = findViewById(R.id.recuperarButton)
        asteriscoTextView = findViewById(R.id.asteriscoTextView)

        recuperarButton.setOnClickListener {
            if(emailEditText.text.toString().isEmpty()) {
                asteriscoTextView.visibility = View.VISIBLE
            }
            else{
                asteriscoTextView.visibility = View.INVISIBLE
                enviarEmail(emailEditText.text.toString())
            }
        }

    }
    private fun enviarEmail(email: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}