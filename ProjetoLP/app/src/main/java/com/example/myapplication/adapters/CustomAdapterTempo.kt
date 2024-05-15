package com.example.myapplication

import com.example.myapplication.models.TempoInformation
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.models.information

class CustomAdapterTempo(
    private val dataSet: ArrayList<TempoInformation>,
    private val context: Context,
) : RecyclerView.Adapter<CustomAdapterTempo.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val diaTextView: TextView = view.findViewById(R.id.diaTextView)
        //val tempoImageView: ImageView = view.findViewById(R.id.tempoImageView)
        val minTempTextView: TextView = view.findViewById(R.id.minTempTextView)
        val maxTempTextView: TextView = view.findViewById(R.id.maxTempTextView)
        val ventoDirectionTextView: TextView = view.findViewById(R.id.ventoDirectionTextView)
        val precipitacaoTextView: TextView = view.findViewById(R.id.precipitacaoTextView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.tempo, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = dataSet[position]

        viewHolder.diaTextView.text = data.dia
        //viewHolder.tempoImageView = data.tempoImage
        viewHolder.minTempTextView.text = data.minTemperatura
        viewHolder.maxTempTextView.text = data.maxTemperatura
        viewHolder.ventoDirectionTextView.text = data.ventoDirection
        viewHolder.precipitacaoTextView.text = data.precipition


        /*Glide.with(context)
            .load(produto.imagem)
            .into(viewHolder.imageView)*/
        /*    val diaTextView: TextView = view.findViewById(R.id.diaTextView)
        val tempoImageView: TextView = view.findViewById(R.id.tempoImageView)
        val minTempTextView: TextView = view.findViewById(R.id.minTempTextView)
        val maxTempTextView: ImageView = view.findViewById(R.id.maxTempTextView)
        val ventoDirectionTextView: TextView = view.findViewById(R.id.ventoDirectionTextView)
        val precipitacaoTextView: TextView = view.findViewById(R.id.precipitacaoTextView)*/
    }

    override fun getItemCount(): Int = dataSet.size
}
