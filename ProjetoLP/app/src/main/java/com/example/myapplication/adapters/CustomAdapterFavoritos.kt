package com.example.myapplication.adapters

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.FavModel
import com.google.android.material.color.MaterialColors.getColor
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.roundToInt

class CustomAdapterFavoritos(
    private val dataSet: ArrayList<FavModel>,
    private val context: Context,
    private val listener: RecyclerViewEvent
) : RecyclerView.Adapter<CustomAdapterFavoritos.ViewHolder>(), Filterable {

    private var musicaListFull: ArrayList<FavModel> = ArrayList(dataSet)

    class ViewHolder(view: View, private val listener: RecyclerViewEvent, private val context: Context) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val localTextView: TextView = view.findViewById(R.id.localTextView)
        val temperaturaMaxTextView: TextView = view.findViewById(R.id.tMax)
        val temperaturaMinTextView: TextView = view.findViewById(R.id.tMin)
        val tempoImageView: ImageView = view.findViewById(R.id.tempoImageView)
        val iconImageView: ImageView = view.findViewById(R.id.estrelaImageView)
        val textView12 : TextView = view.findViewById(R.id.textView12)
        val textView20 : TextView = view.findViewById(R.id.textView20)
        val textView26 : TextView = view.findViewById(R.id.textView26)

        init {
            view.setOnClickListener(this)
            iconImageView.setOnClickListener {
                // Handle star icon click
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onStarIconClick(position)
                }
            }
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.favorito, viewGroup, false)
        return ViewHolder(view, listener, context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val favorito = dataSet[position]

        val sharedPreferences : SharedPreferences
        sharedPreferences = context.getSharedPreferences("def", Context.MODE_PRIVATE)
        val checked = sharedPreferences.getBoolean("switch_checked", false)
        if(checked){
            viewHolder.localTextView.setTextColor(Color.WHITE)
            viewHolder.temperaturaMaxTextView.setTextColor(Color.WHITE)
            viewHolder.temperaturaMinTextView.setTextColor(Color.WHITE)
            viewHolder.textView12.setTextColor(Color.WHITE)
            viewHolder.textView20.setTextColor(Color.WHITE)
            viewHolder.textView26.setTextColor(Color.WHITE)
        }else{
            viewHolder.localTextView.setTextColor(Color.GRAY)
            viewHolder.temperaturaMaxTextView.setTextColor(Color.GRAY)
            viewHolder.temperaturaMinTextView.setTextColor(Color.GRAY)
            viewHolder.textView12.setTextColor(Color.GRAY)
            viewHolder.textView20.setTextColor(Color.GRAY)
            viewHolder.textView26.setTextColor(Color.GRAY)
        }

        viewHolder.localTextView.text = favorito.local
        viewHolder.temperaturaMaxTextView.text = favorito.tmax.toDouble().roundToInt().toString() + "°C"
        viewHolder.temperaturaMinTextView.text = favorito.tmin.toDouble().roundToInt().toString() + "°C"
        imagemTempo(favorito.imagem, viewHolder.tempoImageView)
    }

    private fun imagemTempo(tempoImage: String, tempoImageView: ImageView) {
        if (tempoImage.equals("1")) {
            tempoImageView.setImageResource(R.drawable.w_ic_d_01)
        }else if (tempoImage.equals("2")) {
            tempoImageView.setImageResource(R.drawable.w_ic_d_02)
        }else if (tempoImage.equals("3")) {
            tempoImageView.setImageResource(R.drawable.w_ic_d_03)
        }else if (tempoImage.equals("4")) {
            tempoImageView.setImageResource(R.drawable.w_ic_d_04)
        }else if (tempoImage.equals("5")) {
            tempoImageView.setImageResource(R.drawable.w_ic_d_05)
        }else if(tempoImage.equals("6")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_06)
        }else if(tempoImage.equals("7")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_07)
        }else  if(tempoImage.equals("8")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_08)
        }else if(tempoImage.equals("9")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_09)
        }else if (tempoImage.equals("10")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_10)
        }else if (tempoImage.equals("11")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_11)
        }else if (tempoImage.equals("12")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_12)
        }else if (tempoImage.equals("13")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_13)
        }else if (tempoImage.equals("14")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_14)
        }else if (tempoImage.equals("15")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_15)
        }else if (tempoImage.equals("16")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_16)
        }else if (tempoImage.equals("17")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_17)
        }else if (tempoImage.equals("18")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_18)
        }else if (tempoImage.equals("19")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_19)
        }else if (tempoImage.equals("20")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_20)
        }else if (tempoImage.equals("21")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_21)
        }else if (tempoImage.equals("22")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_22)
        }else if (tempoImage.equals("23")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_23)
        }else if (tempoImage.equals("24")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_24)
        }else if (tempoImage.equals("25")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_25)
        }else if (tempoImage.equals("26")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_26)
        }else if (tempoImage.equals("27")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_27)
        }else if (tempoImage.equals("28")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_28)
        }else if (tempoImage.equals("29")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_29)
        }else if (tempoImage.equals("30")){
            tempoImageView.setImageResource(R.drawable.w_ic_d_30)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    private val musicaFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<FavModel>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(musicaListFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                for (item in musicaListFull) {
                    if (item.local.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            dataSet.clear()
            dataSet.addAll(results?.values as ArrayList<FavModel>)
            notifyDataSetChanged()
        }
    }

    interface RecyclerViewEvent {
        fun onItemClick(position: Int)
        fun onStarIconClick(position: Int)  // Add method for star icon click
    }

    override fun getFilter(): Filter {
        return musicaFilter
    }
}
