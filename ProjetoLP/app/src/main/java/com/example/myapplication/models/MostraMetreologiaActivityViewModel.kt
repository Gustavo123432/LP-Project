package com.example.myapplication.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MostraMetreologiaActivityViewModel(private val repository: Repository): ViewModel() {

    var myCustomPosts: MutableLiveData<Response<List<TempoInformation>>> = MutableLiveData()


   /* fun getCustomPosts(dia: String, tempoImage: Int, minTemperatura: String, maxTemperatura: String, ventoDirection: String, precipition: String) {
        viewModelScope.launch {
            val response = repository.getCustomPosts(dia, tempoImage, minTemperatura, maxTemperatura, ventoDirection, precipition)
            myCustomPosts.value = response
        }
    }*/


}