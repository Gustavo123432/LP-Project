package com.example.myapplication

import com.example.myapplication.models.TempoInformation
import android.content.Context
import android.icu.text.DecimalFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.MarInformation
import com.example.myapplication.models.UvInformation
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.roundToInt

//import com.bumptech.glide.Glide

class CustomAdapterMar(
    private val dataDay: ArrayList<MarInformation>,
    private val dataDay1 : ArrayList<MarInformation>,
    private val dataDay2 : ArrayList<MarInformation>,
    private val globalIdSelecionado: String,
    private val context: Context,
) : RecyclerView.Adapter<CustomAdapterMar.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val periodoMinimoOndaTextView: TextView = view.findViewById(R.id.periodoMinimoOndaTextView)
        val periodoMaximoOndaTextView: TextView = view.findViewById(R.id.periodoMaximoOndaTextView)
        val alturaMinimaMarTextView: TextView = view.findViewById(R.id.alturaMinimaMarTextView)
        val alturaMaximaMarTextView: TextView = view.findViewById(R.id.alturaMaximaMarTextView)
        val ondulacaoMinimaTextView: TextView = view.findViewById(R.id.ondulacaoMinimaTextView)
        val ondulacaoMaximaTextView: TextView = view.findViewById(R.id.ondulacaoMaximaTextView)
        val temperaturaMinimaMarTextView: TextView = view.findViewById(R.id.temperaturaMinimaMarTextView)
        val temperaturaMaximaMarTextView: TextView = view.findViewById(R.id.temperaturaMaximaMarTextView)
        val direcaoOndaTextView: TextView = view.findViewById(R.id.direcaoOndaTextView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.mar, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        for (i in 0 until dataDay.size) {
            if(dataDay[i].globalIdLocal == globalIdSelecionado) {
                viewHolder.periodoMinimoOndaTextView.text = dataDay[i].periodoMinimoOnda
                viewHolder.periodoMaximoOndaTextView.text = dataDay[i].periodoMaximoOnda
                viewHolder.alturaMinimaMarTextView.text = dataDay[i].marTotalMinimo.toString()
                viewHolder.alturaMaximaMarTextView.text = dataDay[i].marTotalMaximo.toString()
                viewHolder.ondulacaoMinimaTextView.text = dataDay[i].ondulacaoMinima
                viewHolder.ondulacaoMaximaTextView.text = dataDay[i].ondulacaoMax
                viewHolder.temperaturaMinimaMarTextView.text = dataDay[i].temperaturaMinimaMar
                viewHolder.temperaturaMaximaMarTextView.text = dataDay[i].temperaturaMaximaMar
                viewHolder.direcaoOndaTextView.text = dataDay[i].direcaoOnda
            }
        }
        for (i in 0 until dataDay1.size) {
            if(dataDay1[i].globalIdLocal == globalIdSelecionado) {
                viewHolder.periodoMinimoOndaTextView.text = dataDay1[i].periodoMinimoOnda
                viewHolder.periodoMaximoOndaTextView.text = dataDay1[i].periodoMaximoOnda
                viewHolder.alturaMinimaMarTextView.text = dataDay1[i].marTotalMinimo.toString()
                viewHolder.alturaMaximaMarTextView.text = dataDay1[i].marTotalMaximo.toString()
                viewHolder.ondulacaoMinimaTextView.text = dataDay1[i].ondulacaoMinima
                viewHolder.ondulacaoMaximaTextView.text = dataDay1[i].ondulacaoMax
                viewHolder.temperaturaMinimaMarTextView.text = dataDay1[i].temperaturaMinimaMar
                viewHolder.temperaturaMaximaMarTextView.text = dataDay1[i].temperaturaMaximaMar
                viewHolder.direcaoOndaTextView.text = dataDay1[i].direcaoOnda
            }
        }
        for (i in 0 until dataDay2.size) {
            if(dataDay2[i].globalIdLocal == globalIdSelecionado) {
                viewHolder.periodoMinimoOndaTextView.text = dataDay2[i].periodoMinimoOnda
                viewHolder.periodoMaximoOndaTextView.text = dataDay2[i].periodoMaximoOnda
                viewHolder.alturaMinimaMarTextView.text = dataDay2[i].marTotalMinimo.toString()
                viewHolder.alturaMaximaMarTextView.text = dataDay2[i].marTotalMaximo.toString()
                viewHolder.ondulacaoMinimaTextView.text = dataDay2[i].ondulacaoMinima
                viewHolder.ondulacaoMaximaTextView.text = dataDay2[i].ondulacaoMax
                viewHolder.temperaturaMinimaMarTextView.text = dataDay2[i].temperaturaMinimaMar
                viewHolder.temperaturaMaximaMarTextView.text = dataDay2[i].temperaturaMaximaMar
                viewHolder.direcaoOndaTextView.text = dataDay2[i].direcaoOnda
            }
        }

    }


    override fun getItemCount(): Int = dataDay.size
}
