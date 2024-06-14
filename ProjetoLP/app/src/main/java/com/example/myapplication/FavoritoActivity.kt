package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.CustomAdapterFavoritos
import com.example.myapplication.definicoes.DefinicoesActivity
import com.example.myapplication.models.FavModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class FavoritoActivity : AppCompatActivity(), CustomAdapterFavoritos.RecyclerViewEvent {

    private lateinit var textView7: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private  lateinit var sharedPreferences: SharedPreferences
    private val listaFav = ArrayList<FavModel>()
    private lateinit var favCustomAdapter: CustomAdapterFavoritos
    private lateinit var searchView: SearchView
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorito)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textView7 = findViewById(R.id.textView7)
        recyclerView = findViewById(R.id.favoritoRecyclerView)
        bottomNavigationView = findViewById(R.id.favBottomNavigationView)
        searchView = findViewById(R.id.searchView)
        constraintLayout = findViewById(R.id.main)

        sharedPreferences = getSharedPreferences("def", Context.MODE_PRIVATE)
        val checked = sharedPreferences.getBoolean("switch_checked", false)
        if(checked){
            textView7.setTextColor(Color.WHITE)
            constraintLayout.setBackgroundColor(getColor(R.color.cinzaEscuro))
        }else{
            textView7.setTextColor(Color.GRAY)
            constraintLayout.setBackgroundColor(getColor(R.color.white))
        }

        sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString("uidProfile", "")

        getDados(this, uid.toString())

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                favCustomAdapter.filter.filter(newText)
                return false
            }
        })

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
    fun getDados(context: Context, uid: String) {

        val databaseReference = FirebaseDatabase.getInstance().getReference()

        databaseReference.child("Perfil").child(uid.toString()).child("locais").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val lista = it.getValue(FavModel::class.java)
                    if (lista != null) {
                        listaFav.add(lista)
                    }
                }
                favCustomAdapter =
                    CustomAdapterFavoritos(listaFav, context, this@FavoritoActivity)
                recyclerView.layoutManager =
                    LinearLayoutManager(this@FavoritoActivity, LinearLayoutManager.VERTICAL, false)
                recyclerView.adapter = favCustomAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PaginaPrincipalActivity", "Database operation cancelled: $error")
            }
        })
    }

    override fun onItemClick(position: Int) {
        //onStarIconClick(position)
        Log.d("TAG", "onItemClick: $position")
    }

    override fun onStarIconClick(position: Int) {
        val item = listaFav[position]

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Deseja remover este local?")
            .setPositiveButton("Sim") { dialog, id ->
                // User clicked OK button
                removeItemFromDatabase(
                    position)
                listaFav.removeAt(position)
                favCustomAdapter.notifyItemRemoved(position)
                Toast.makeText(this, "Local removido", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Não", null)
        builder.create().show()
    }

    private fun removeItemFromDatabase(position: Int) {
        sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString("uidProfile", "")
        uid?.let {
            val databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Perfil").child(uid).child("locais")

            // Fetch the current array
            databaseReference.get().addOnSuccessListener { dataSnapshot ->
                val locaisList = dataSnapshot.getValue(object : GenericTypeIndicator<List<Map<String, String>>?>() {}) ?: return@addOnSuccessListener

                if (locaisList.size > position) {
                    // Create a new list without the item at the specified position
                    val updatedList = locaisList.toMutableList()
                    updatedList.removeAt(position+1)

                    // Update the database with the new list
                    databaseReference.setValue(updatedList).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Local removido do banco de dados", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Falha ao remover local do banco de dados", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Posição inválida", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Falha ao acessar o banco de dados", Toast.LENGTH_SHORT).show()
            }
        }
    }

}