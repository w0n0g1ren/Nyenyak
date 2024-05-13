package com.dicoding.nyenyak.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

object SleepTimeCalculator {

    @RequiresApi(Build.VERSION_CODES.O)
    fun DurasiIdeal (umur: Int): Pair<Int, Int>{
        var durasiMin: Int = 0
        var durasiMax: Int = 0

        when{
            (umur < 1) -> {
                durasiMin = 12
                durasiMax = 16
            }
            (umur >= 1 && umur <= 2) -> {
                durasiMin = 11
                durasiMax = 14
            }
            (umur >= 3 && umur <= 5) -> {
                durasiMin = 10
                durasiMax = 13
            }
            (umur >= 6 && umur <= 12) -> {
                durasiMin = 9
                durasiMax = 12
            }
            (umur >= 13 && umur <= 18) -> {
                durasiMin = 8
                durasiMax = 10
            }
            (umur > 18) -> {
                durasiMin = 7
                durasiMax = 8
            }
        }
        return durasiMin to durasiMax
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Calculate (aktifitas: String, jam: Int, umur: Int): Pair<Int, Int>{
        Log.e("umur","$umur")
        var (min, max) = DurasiIdeal(umur)
        var waktuMin: Int = 0
        var waktuMax: Int = 0

        Log.e("durasi","$min , $max")

        var selectedHourMin: Int = 0
        var selectedHourMax: Int = 0

        if (aktifitas == "tidur"){
            selectedHourMin = min + jam
            selectedHourMax = max + jam

            if (selectedHourMin > 24 && selectedHourMax > 24){
                waktuMin = selectedHourMin - 24
                waktuMax = selectedHourMax - 24
            }
            else{
                waktuMin = selectedHourMin
                waktuMax = selectedHourMax
            }
        }
        else{
            selectedHourMin = jam - min
            selectedHourMax = jam - max

            if (selectedHourMin < 0 && selectedHourMax < 0){
                waktuMin = 24 + selectedHourMin
                waktuMax = 24 + selectedHourMax
            }
            else{
                waktuMin = selectedHourMin
                waktuMax = selectedHourMax
            }
        }
        return waktuMin to waktuMax
    }

}