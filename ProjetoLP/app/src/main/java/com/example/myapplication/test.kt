package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class test : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.example.myapplication.R.layout.activity_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.example.myapplication.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageView = findViewById(com.example.myapplication.R.id.imageView3)
        button = findViewById(com.example.myapplication.R.id.button)
        button.setOnClickListener {
            escolha()
        }
    }


    //add foto to ImageView and capture automatic
    private fun escolha() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Selecione a Ação")
        val pictureDialogItems = arrayOf("Selecionar da Galeria", "Abrir Câmera")
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> selectPhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun selectPhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryActivityResultLauncher.launch(galleryIntent)
    }

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val contentURI: Uri? = data?.data
            contentURI?.let {
                imageView.setImageURI(it)
            }
        }
    }

    // Declaração da variável para armazenar o código da imagem capturada
    private val REQUEST_IMAGE_CAPTURE = 1

    // Função para tirar a foto usando a câmera
    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    // Método para lidar com o resultado da captura da imagem
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap) // Define o bitmap na imageView
        }
    }


}
