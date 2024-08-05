package com.dicoding.nyenyak.ui.fragment.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.response.ArticleResponseItem
import com.dicoding.nyenyak.data.response.GetDiagnosisResponseItem

class DashboardFragmentViewModel(private val repository: AppRepository): ViewModel() {

    var getDiagnosisResponseItem: LiveData<List<GetDiagnosisResponseItem>> = repository.getDiagnosisResponseItem.distinctUntilChanged()
    var getArticleResponseItem: LiveData<List<ArticleResponseItem>> = repository.getArticleResponseItem.distinctUntilChanged()
    fun getDiagnosis(){
        return repository.getDiagnosis()
    }

    fun getArticle(){
        return repository.getArticle()
    }
}

