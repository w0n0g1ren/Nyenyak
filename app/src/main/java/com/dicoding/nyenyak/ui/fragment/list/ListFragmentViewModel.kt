package com.dicoding.nyenyak.ui.fragment.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.response.GetDiagnosisResponseItem

class ListFragmentViewModel (private val repository: AppRepository): ViewModel() {
    var getDiagnosisResponseItem: LiveData<List<GetDiagnosisResponseItem>> = repository.getDiagnosisResponseItem.distinctUntilChanged()

    fun getDiagnosis(){
        return repository.getDiagnosis()
    }
}

