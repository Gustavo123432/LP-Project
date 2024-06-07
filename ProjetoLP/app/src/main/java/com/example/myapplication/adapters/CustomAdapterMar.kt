package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.MarInformation

class CustomAdapterMar(
    private val dataDay: List<MarInformation>,
    private val dataDay1: List<MarInformation>,
    private val dataDay2: List<MarInformation>,
    private val globalIdSelecionado: String,
    private val context: Context,
) : RecyclerView.Adapter<CustomAdapterMar.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dataTextView: TextView = view.findViewById(R.id.dataTextView)
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
        val marInformation: MarInformation? = when (position) {
            0 -> dataDay.find { it.globalIdLocal == globalIdSelecionado }
            1 -> dataDay1.find { it.globalIdLocal == globalIdSelecionado }
            2 -> dataDay2.find { it.globalIdLocal == globalIdSelecionado }
            else -> null
        }

        marInformation?.let {
            viewHolder.dataTextView.text = it.dia
            viewHolder.periodoMinimoOndaTextView.text = it.periodoMinimoOnda
            viewHolder.periodoMaximoOndaTextView.text = it.periodoMaximoOnda
            viewHolder.alturaMinimaMarTextView.text = it.marTotalMinimo.toString()
            viewHolder.alturaMaximaMarTextView.text = it.marTotalMaximo.toString()
            viewHolder.ondulacaoMinimaTextView.text = it.ondulacaoMinima
            viewHolder.ondulacaoMaximaTextView.text = it.ondulacaoMax
            viewHolder.temperaturaMinimaMarTextView.text = it.temperaturaMinimaMar
            viewHolder.temperaturaMaximaMarTextView.text = it.temperaturaMaximaMar
            viewHolder.direcaoOndaTextView.text = it.direcaoOnda
        }
    }

    override fun getItemCount(): Int = 3
}
