package com.example.myapplication.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.Repository

class MostraMetreolgiaActivityViewModelFactory (private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MostraMetreologiaActivityViewModel(repository) as T
    }
}