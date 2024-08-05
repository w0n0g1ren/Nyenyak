package com.dicoding.nyenyak.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.response.DeleteResponse
import com.dicoding.nyenyak.data.response.GetDiagnosisResponseItem

class DetailViewModel(private val repository: AppRepository): ViewModel() {
    var getDetailDiagnosisResponse: LiveData<GetDiagnosisResponseItem> = repository.getDetailDiagnosisResponseItem

    fun getDetailDiagnosis(uid: String){
        return repository.getDetailDiagnosis(uid)
    }

    suspend fun deleteDiagnosis(uid: String): DeleteResponse {
        return repository.deleteDiagnosis(uid)
    }
}

