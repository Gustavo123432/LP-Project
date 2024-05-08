package pt.epvc.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.TempoInformation

class CustomAdapterTempo(
    private val dataSet: ArrayList<TempoInformation>,
    private val context: Context,
) : RecyclerView.Adapter<CustomAdapterTempo.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dataTextView: TextView = view.findViewById(R.id.diaTextView)
        val tempoImagemImageView: ImageView = view.findViewById(R.id.tempoImageView)
        val tMinTextView: TextView = view.findViewById(R.id.minTempTextView)
        val tMaxTextView: TextView = view.findViewById(R.id.maxTempTextView)
        val direcaoVentoTextView: TextView = view.findViewById(R.id.ventoDirectionTextView)
        val probabilidadePrecipitacaoTextView: TextView = view.findViewById(R.id.precipitacaoTextView)


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.tempo, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val dados = dataSet[position]

        viewHolder.dataTextView.text = dados.forecastDate
        viewHolder.tMinTextView.text = dados.minTemp
        viewHolder.tMaxTextView.text = dados.maxTemp
        viewHolder.direcaoVentoTextView.text = dados.predWindDir
        viewHolder.probabilidadePrecipitacaoTextView.text = dados.precipitaProb


    }

    override fun getItemCount(): Int = dataSet.size
}
