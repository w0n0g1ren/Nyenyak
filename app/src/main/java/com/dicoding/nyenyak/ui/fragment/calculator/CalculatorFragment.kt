package com.dicoding.nyenyak.ui.fragment.calculator

import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AnalogClock
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.data.api.ApiConfig
import com.dicoding.nyenyak.data.response.GetDetailUserResponse
import com.dicoding.nyenyak.databinding.FragmentCalculatorBinding
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.session.datastore
import com.dicoding.nyenyak.ui.SecondViewModelFactory
import com.dicoding.nyenyak.ui.fragment.user.UserFragment
import com.dicoding.nyenyak.ui.fragment.user.UserFragmentViewModel
import com.dicoding.nyenyak.ui.main.MainActivity
import com.dicoding.nyenyak.utils.SleepTimeCalculator
import com.github.naz013.analoguewatch.AnalogueClockView
import com.github.naz013.analoguewatch.TimeData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date


class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!
    private var aktifitas: String = ""
    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0
    private var umur: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        val root: View = binding.root
        getUmur()
        setupAction()
        return root
    }

    private fun getUmur() {
        var dapatUmur: String = ""
        val pref = SessionPreference.getInstance(requireContext().datastore)
        val viewModel =
            (context as? MainActivity)?.let {
                ViewModelProvider(it, SecondViewModelFactory(pref)).get(
                    UserFragmentViewModel::class.java
                )
            }
        (context as? MainActivity)?.let {
            viewModel?.getToken()?.observe(it){
                if (it.token != null){
                    val apiService = ApiConfig.getApiService(it.token).getUser()
                    apiService.enqueue(object : Callback<GetDetailUserResponse> {
                        override fun onResponse(
                            call: Call<GetDetailUserResponse>,
                            response: Response<GetDetailUserResponse>
                        ) {
                            if (response.isSuccessful){
                                val responseBody = response.body()
                                if (responseBody != null){
                                    umur = responseBody.user!!.age!!.toInt()
                                    Log.e(TAG,"$umur")
                                }else{
                                    Log.e(TAG, "onFailure: ${response.message()}")
                                }
                            }
                            else{
                                val errorcode : String = response.code().toString()
                                when(errorcode){
                                    "401" -> {
                                    }
                                }

                            }
                        }

                        override fun onFailure(call: Call<GetDetailUserResponse>, t: Throwable) {
                            Log.e(TAG, "onFailure: ${t.message}")
                        }
                    })
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupAction() {
        binding.materialButtonToggleGroup.addOnButtonCheckedListener{toggleButton, checkedId, isChecked->
            if(isChecked){
                when (checkedId){
                    R.id.bangun -> {
                        context?.getColor(R.color.dark_purple)
                            ?.let { binding.bangun.setTextColor(it) }
                        context?.let { binding.tidur.setTextColor(it.getColor(R.color.white)) }
                       aktifitas = "bangun"
                    }

                    R.id.tidur -> {
                        context?.getColor(R.color.dark_purple)
                            ?.let { binding.tidur.setTextColor(it) }
                        context?.let { binding.bangun.setTextColor(it.getColor(R.color.white)) }
                        aktifitas = "tidur"
                    }
                }
            }
        }
        binding.jamIv.setOnClickListener {

            val timePickerDialog = TimePickerDialog(
                context as MainActivity,
                { _, hourOfDay, minute ->
                    selectedHour = hourOfDay
                    selectedMinute = minute
                    
                    val format = SimpleDateFormat("HH:mm")
                    var sekarang = Date(0,0,0,selectedHour,selectedMinute,0)
                    val formatdate = format.format(sekarang)
                    binding.tvJamKal.text = formatdate.toString()
                },
                0,
                0,
                true
            )
            timePickerDialog.show()

        }
        binding.btnKalkulasi.setOnClickListener {
            var cekJam = binding.tvJamKal.text
            when{
                aktifitas.isEmpty() -> Toast.makeText(context as MainActivity,
                    "tolong pilih aktifitas yang diinginkan",Toast.LENGTH_SHORT).show()
                cekJam == getString(R.string._00_00) -> Toast.makeText(context as MainActivity,
                    "tolong pilih waktu yang diinginkan",Toast.LENGTH_SHORT).show()
            }

            val dialog = Dialog(context as MainActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.kalkulator_popup_layout)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setDimAmount(0.0f)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)

            val jamBangun : TextView = dialog.findViewById(R.id.jam_bangun)
            val jamTidur : TextView = dialog.findViewById(R.id.jam_tidur)
            val durasi : TextView = dialog.findViewById(R.id.tv_durasi)
            val cancel : ImageView = dialog.findViewById(R.id.cancelDialog)
            val clockSleep : AnalogueClockView = dialog.findViewById(R.id.analogueClockViewSleep)
            val clockWake : AnalogueClockView = dialog.findViewById(R.id.analogueClockViewWake)

            var (min,max) = SleepTimeCalculator.Calculate(aktifitas, selectedHour, umur)
            var (minJam,maxJam) = SleepTimeCalculator.DurasiIdeal(umur)

            val format = SimpleDateFormat("HH:mm")
            var jamMin = Date(0,0,0,min,selectedMinute,0)
            var jamMax = Date(0,0,0,max,selectedMinute,0)

            var jamTampilMin = format.format(jamMin)
            var jamTampilMax = format.format(jamMax)

            if (aktifitas == "bangun"){
                jamBangun.text = binding.tvJamKal.text.toString()
                jamTidur.text = "$jamTampilMin - \n $jamTampilMax"
                clockSleep.setTime(TimeData(min,selectedMinute,0))
                clockWake.setTime(TimeData(selectedHour,selectedMinute,0))
            }else{
                jamTidur.text = binding.tvJamKal.text.toString()
                jamBangun.text = "$jamTampilMin - \n $jamTampilMax"
                clockSleep.setTime(TimeData(selectedHour,selectedMinute,0))
                clockWake.setTime(TimeData(max,selectedMinute,0))
            }
            durasi.text = "$minJam - $maxJam Jam"

            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    companion object{
        private const val TAG = "CalculatorFragment"
    }
}