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
    private val dataSet: ArrayList<MarInformation>,
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
        for (i in 0 until dataSet.size) {
            if(dataSet[i].globalIdLocal.equals(globalIdSelecionado)) {
                viewHolder.periodoMinimoOndaTextView.text = dataSet[i].periodoMinimoOnda
                viewHolder.periodoMaximoOndaTextView.text = dataSet[i].periodoMaximoOnda
                viewHolder.alturaMinimaMarTextView.text = dataSet[i].marTotalMinimo
                viewHolder.alturaMaximaMarTextView.text = dataSet[i].marTotalMaximo
                viewHolder.ondulacaoMinimaTextView.text = dataSet[i].ondulacaoMinima
                viewHolder.ondulacaoMaximaTextView.text = dataSet[i].ondulacaoMax
                viewHolder.temperaturaMinimaMarTextView.text = dataSet[i].temperaturaMinimaMar
                viewHolder.temperaturaMaximaMarTextView.text = dataSet[i].temperaturaMaximaMar
                viewHolder.direcaoOndaTextView.text = dataSet[i].direcaoOnda
            }
        }

    }


    override fun getItemCount(): Int = dataSet.size
}
