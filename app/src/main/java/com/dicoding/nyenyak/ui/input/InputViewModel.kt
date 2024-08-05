package com.dicoding.nyenyak.ui.input

import androidx.lifecycle.ViewModel
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.response.InputResponse

class InputViewModel(private val repository: AppRepository): ViewModel() {

    suspend fun inputDiagnosis(
        weight: Int, height: Int,
        sleepDuration: Float, qualityOfSleep: Int,
        physicalActivityLevel: Int, bloodPressure: String,
        stressLevel: Int, heartRate: Int, dailySteps: Int): InputResponse{
        return repository.inputDiagnosis(
            weight, height,
            sleepDuration, qualityOfSleep,
            physicalActivityLevel, bloodPressure,
            stressLevel, heartRate, dailySteps)
    }
}
