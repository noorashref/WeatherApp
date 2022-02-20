package com.example.weathermvvm

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.WeatherResponse
import com.example.weathermvvm.databinding.ActivityMainBinding
import com.example.weathermvvm.viewmodel.WeatherViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val cloud = "15"
    private val sun = "16"

    private  val viewModel : WeatherViewModel by viewModels()

    private lateinit var binding : ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            //viewModel.refreshData("Edinburgh")
            viewModel.weather_data.observe(this
            ) {
                    data ->
                data?.let {

                    binding.tvDegree.text = getCelsius(data.list.get(0).main.temp)
                    degreeBackground(it)

                    binding.tvCityName.text = data.city.name.toString()
                    cloudIcon(it)

                    binding.tvDegreeCountry.text = data.city.country.toString()
                    binding.tvDegreeCity.text = data.city.name.toString()

                    lastLayer(it)
                }
            }

            binding.imgSearchCity.setOnClickListener {
                val cityName = binding.edtCityName.text.toString()
                viewModel.refreshData(cityName)
                getLiveStatus()
            }





        }
    fun getCelsius(kelvin:Double):String {
        return String.format("%.2f", kelvin-273.15)+" °C"
    }

    fun cloudIcon(weatherResponse:WeatherResponse){

        if(getCelsius(weatherResponse.list.get(0).main.temp) > cloud){
            binding.imgWeatherPictures.setBackgroundResource(R.drawable.ic_baseline_wb_sunny_24)
        }else{
            binding.imgWeatherPictures.setBackgroundResource(R.drawable.ic_baseline_cloud_queue_24)
        }
    }

    fun lastLayer(weatherResponse: WeatherResponse){
        binding.tvHumidity.text = weatherResponse.list.get(0).main.humidity.toString()
        binding.tvWindSpeed.text = weatherResponse.list.get(0).main.pressure.toString()
        binding.tvLat.text = weatherResponse.city.coord.lat.toString()
        binding.tvLon.text = weatherResponse.city.coord.lon.toString()
    }

    fun degreeBackground(weatherResponse: WeatherResponse){
        if (getCelsius(weatherResponse.list.get(0).main.temp) > cloud) {
            binding.tvDegree.setBackgroundResource(R.drawable.sunny)
        } else {
            binding.tvDegree.setBackgroundResource(R.drawable.cld)
        }
    }

    private fun getLiveStatus() {

        viewModel.weather_error.observe(this) { error ->
            error?.let {
                if (error) {
                    binding.tvError.visibility = View.VISIBLE
                    binding.pbLoading.visibility = View.GONE
                    binding.llData.visibility = View.GONE
                } else {
                    binding.tvError.visibility = View.GONE
                }
            }
        }

        viewModel.weather_loading.observe(this) { loading ->
            loading?.let {
                if (loading) {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                    binding.llData.visibility = View.GONE
                } else {
                    binding.pbLoading.visibility = View.GONE
                }
            }
        }

    }

}


//90E0EF