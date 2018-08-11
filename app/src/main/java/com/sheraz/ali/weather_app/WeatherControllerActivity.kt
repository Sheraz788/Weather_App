package com.sheraz.ali.weather_app

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

import com.sheraz.ali.weather_app.Utilities.MIN_DISTANCE
import com.sheraz.ali.weather_app.Utilities.MIN_TIME
import com.sheraz.ali.weather_app.Utilities.REQUEST_CODE
import com.sheraz.ali.weather_app.Utilities.WEATHER_URL
import cz.msebera.android.httpclient.Header
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

    }

    override fun onResume() {
        super.onResume()

        getWeatherForCurrentLocation();
    }

    private fun getWeatherForCurrentLocation() {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location?) {

                Log.d("Weather", "onLocationChanged is Called...")

                val longitude = location?.longitude.toString()
                val latitude = location?.latitude.toString()

                val params = RequestParams()
                params.put("log", longitude)
                params.put("lat", latitude)
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
            if(permissions.size > 0 && grantResults[0] != PackageManager.PERMISSION_DENIED){
                Log.d("weather", "Success : Permission Granted")
            }else{
                Log.d("weather", "Failed : Permission Denied")
            }
        }


    }

    //Networking
    fun letDoNetworking(params : RequestParams){

        val client = AsyncHttpClient()

        client.get(WEATHER_URL, params, object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                super.onSuccess(statusCode, headers, response)

                Log.d("WEATHER JSON", response.toString())


            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                super.onFailure(statusCode, headers, throwable, errorResponse)


            }
        })

    }

}

