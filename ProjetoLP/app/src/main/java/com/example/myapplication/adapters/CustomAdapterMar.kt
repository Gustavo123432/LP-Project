package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
        val textView1: TextView = view.findViewById(R.id.textView6)
        val textView2: TextView = view.findViewById(R.id.textView9)
        val textView3: TextView = view.findViewById(R.id.textView11)
        val textView4: TextView = view.findViewById(R.id.textView13)
        val textView5: TextView = view.findViewById(R.id.textView15)
        val textView6: TextView = view.findViewById(R.id.textView17)
        val textView7: TextView = view.findViewById(R.id.textView19)
        val textView8: TextView = view.findViewById(R.id.textView21)
        val textView9: TextView = view.findViewById(R.id.textView23)
        val constraintLayout: ConstraintLayout = view.findViewById(R.id.mainMar)

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
            val sharedPreferences : SharedPreferences
            sharedPreferences = context.getSharedPreferences("def", Context.MODE_PRIVATE)
            val checked = sharedPreferences.getBoolean("switch_checked", false)
            if (checked) {
                viewHolder.dataTextView.setTextColor(Color.WHITE)
                viewHolder.periodoMaximoOndaTextView.setTextColor(Color.WHITE)
                viewHolder.periodoMinimoOndaTextView.setTextColor(Color.WHITE)
                viewHolder.alturaMaximaMarTextView.setTextColor(Color.WHITE)
                viewHolder.alturaMinimaMarTextView.setTextColor(Color.WHITE)
                viewHolder.ondulacaoMaximaTextView.setTextColor(Color.WHITE)
                viewHolder.ondulacaoMinimaTextView.setTextColor(Color.WHITE)
                viewHolder.temperaturaMaximaMarTextView.setTextColor(Color.WHITE)
                viewHolder.temperaturaMinimaMarTextView.setTextColor(Color.WHITE)
                viewHolder.direcaoOndaTextView.setTextColor(Color.WHITE)
                viewHolder.textView1.setTextColor(Color.WHITE)
                viewHolder.textView2.setTextColor(Color.WHITE)
                viewHolder.textView3.setTextColor(Color.WHITE)
                viewHolder.textView4.setTextColor(Color.WHITE)
                viewHolder.textView5.setTextColor(Color.WHITE)
                viewHolder.textView6.setTextColor(Color.WHITE)
                viewHolder.textView7.setTextColor(Color.WHITE)
                viewHolder.textView8.setTextColor(Color.WHITE)
                viewHolder.textView9.setTextColor(Color.WHITE)
            } else {
                viewHolder.dataTextView.setTextColor(Color.BLACK)
                viewHolder.periodoMaximoOndaTextView.setTextColor(Color.GRAY)
                viewHolder.periodoMinimoOndaTextView.setTextColor(Color.GRAY)
                viewHolder.alturaMaximaMarTextView.setTextColor(Color.GRAY)
                viewHolder.alturaMinimaMarTextView.setTextColor(Color.GRAY)
                viewHolder.ondulacaoMaximaTextView.setTextColor(Color.GRAY)
                viewHolder.ondulacaoMinimaTextView.setTextColor(Color.GRAY)
                viewHolder.temperaturaMaximaMarTextView.setTextColor(Color.GRAY)
                viewHolder.temperaturaMinimaMarTextView.setTextColor(Color.GRAY)
                viewHolder.direcaoOndaTextView.setTextColor(Color.GRAY)
                viewHolder.textView1.setTextColor(Color.BLACK)
                viewHolder.textView2.setTextColor(Color.BLACK)
                viewHolder.textView3.setTextColor(Color.BLACK)
                viewHolder.textView4.setTextColor(Color.BLACK)
                viewHolder.textView5.setTextColor(Color.BLACK)
                viewHolder.textView6.setTextColor(Color.BLACK)
                viewHolder.textView7.setTextColor(Color.BLACK)
                viewHolder.textView8.setTextColor(Color.BLACK)
                viewHolder.textView9.setTextColor(Color.BLACK)


            }
            viewHolder.dataTextView.text = it.dia
            viewHolder.periodoMinimoOndaTextView.text = it.periodoMinimoOnda.replace(".", ",")+"0"
            viewHolder.periodoMaximoOndaTextView.text = it.periodoMaximoOnda.replace(".", ",")+"0"
            viewHolder.alturaMinimaMarTextView.text = it.marTotalMinimo.toString().replace(".", ",")+"0"+"m"
            viewHolder.alturaMaximaMarTextView.text = it.marTotalMaximo.toString().replace(".", ",")+"0"+"m"
            viewHolder.ondulacaoMinimaTextView.text = it.ondulacaoMinima.replace(".", ",")+"0"
            viewHolder.ondulacaoMaximaTextView.text = it.ondulacaoMax.replace(".", ",")+"0"
            viewHolder.temperaturaMinimaMarTextView.text = it.temperaturaMinimaMar+"ºC".replace(".", ",")
            viewHolder.temperaturaMaximaMarTextView.text = it.temperaturaMaximaMar+"ºC".replace(".", ",")
            viewHolder.direcaoOndaTextView.text = it.direcaoOnda
        }
    }

    override fun getItemCount(): Int = 3


}
