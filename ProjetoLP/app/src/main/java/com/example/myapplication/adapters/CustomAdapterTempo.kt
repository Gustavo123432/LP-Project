package com.example.myapplication

import com.example.myapplication.models.TempoInformation
import android.content.Context
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.roundToInt

//import com.bumptech.glide.Glide

class CustomAdapterTempo(
    private val dataSet: ArrayList<TempoInformation>,
    private val context: Context,
) : RecyclerView.Adapter<CustomAdapterTempo.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val diaTextView: TextView = view.findViewById(R.id.diaTextView)
        val tempoImageView: ImageView = view.findViewById(R.id.tempoImageView)
        val minTempTextView: TextView = view.findViewById(R.id.minTempTextView)
        val maxTempTextView: TextView = view.findViewById(R.id.maxTempTextView)
        val ventoImageView: ImageView = view.findViewById(R.id.ventoImageView)
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
        imagemTempo(data.tempoImage, viewHolder.tempoImageView)
        imagemVento(data.ventoDirection, viewHolder.ventoImageView)
        viewHolder.minTempTextView.text = data.minTemperatura.toDouble().roundToInt().toString()
        viewHolder.maxTempTextView.text = data.maxTemperatura.toDouble().roundToInt().toString()
        viewHolder.ventoDirectionTextView.text = data.ventoDirection
        viewHolder.precipitacaoTextView.text = data.precipition


    }

    private fun imagemVento(ventoDirection: String, ventoImageView: ImageView) {
        if (ventoDirection.equals("N")){
            ventoImageView.setImageResource(R.drawable.n)
        }else if (ventoDirection.equals("NE")){
            ventoImageView.setImageResource(R.drawable.ne)
        }else if (ventoDirection.equals("E")){
            ventoImageView.setImageResource(R.drawable.e)
        }else if (ventoDirection.equals("SE")){
            ventoImageView.setImageResource(R.drawable.se)
        }else if (ventoDirection.equals("S")){
            ventoImageView.setImageResource(R.drawable.s)
        }else if (ventoDirection.equals("SW")){
            ventoImageView.setImageResource(R.drawable.sw)
        }else if (ventoDirection.equals("W")){
            ventoImageView.setImageResource(R.drawable.w)
        }else if (ventoDirection.equals("NW")){
            ventoImageView.setImageResource(R.drawable.nw)
        }

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
}
