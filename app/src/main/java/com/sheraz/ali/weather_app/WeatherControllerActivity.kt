package com.sheraz.ali.weather_app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.sheraz.ali.weather_app.Utilities.*

import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_change_city.*
import kotlinx.android.synthetic.main.activity_weather_controller.*
import org.json.JSONObject

class WeatherControllerActivity : AppCompatActivity() {



    //Location Provider
    var LOCATION_PROVIDER : String = LocationManager.GPS_PROVIDER


    //Location Manager and Location Listener
    var locationManager : LocationManager? = null
    var locationListener : LocationListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_controller)


        cityChangeBtn.setOnClickListener {
            val changeCityIntent = Intent(this, change_city::class.java)
            startActivity(changeCityIntent)
        }

    }

    override fun onResume() {
        super.onResume()

        val city= intent
        val cityName = city.getStringExtra("cityName")

        if(cityName != null){

            Log.d("Weather", "onResume() called getting weather for new City")
            updateCityName(cityName)

        }else{

            Log.d("Weather", "onResume() called getting weather for current location")
            getWeatherForCurrentLocation()

        }

    }

    private fun updateCityName(city : String){

        val params = RequestParams()
        params.put("q", city)
        params.put("appid", APP_ID)
        letDoNetworking(params)

    }

    private fun getWeatherForCurrentLocation() {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location?) {

                Log.d("Weather", "onLocationChanged is Called...")

                val longitude = location?.longitude.toString()
                val latitude = location?.latitude.toString()

                val params = RequestParams()
                params.put("lon", longitude)
                params.put("lat", latitude)
                params.put("appid", APP_ID)
                letDoNetworking(params)

            }

            override fun onStatusChanged(s: String?, p1: Int, p2: Bundle?) {

            }

            override fun onProviderEnabled(s: String?) {

            }

            override fun onProviderDisabled(s: String?) {
                Log.d("Weather", "onProviderDisabled() is called...")
            }


        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)

            return
        }

        locationManager?.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener)


    }

    //On Result of Permission Request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE){
            if(permissions.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_DENIED){
                Log.d("weather", "Success : Permission Granted")
            }else{
                Log.d("weather", "Failed : Permission Denied")
            }
        }


    }

    //Networking
    private fun letDoNetworking(params : RequestParams){

        val client = AsyncHttpClient()

        client.get(WEATHER_URL, params, object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject) {
                super.onSuccess(statusCode, headers, response)

                Log.d("WEATHER JSON", response.toString())

                val weather = WeatherDataModel()
                val returnData = weather.fromJSON(response)
                updateUI(returnData)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                super.onFailure(statusCode, headers, throwable, errorResponse)

                Log.d("Weather", "Failed" + throwable.toString())
                Log.d("Weather", "Status Code $statusCode")
                Log.d("Weather" , "Response Error + $errorResponse")

                Toast.makeText(this@WeatherControllerActivity, "", Toast.LENGTH_LONG).show()

            }


        })

    }


    fun updateUI(weatherData: WeatherDataModel){


        cityTemp.text = weatherData.cityTemperature()

        val resourceID = resources.getIdentifier(weatherData.weatherIcon(), "drawable", packageName)
        weatherIcon.setImageResource(resourceID)

        locationName.text = weatherData.cityName()

    }

}

