package com.sheraz.ali.weather_app

import org.json.JSONObject

/**
 * Created by Mega Tech on 8/10/2018.
 */
class WeatherDataModel {

    //Date members
     var temperature : String = ""
     var condition : Int = 0
     var city : String = ""
     var weatherIcon : String = ""

    //Parsing Json data...

    fun fromJSON (jsonObject: JSONObject) : WeatherDataModel {


        val weatherData = WeatherDataModel()


        weatherData.city = jsonObject.getString("name")

        weatherData.condition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id")
        weatherData.weatherIcon = updateWeatherIcon(weatherData.condition)

        var temp : Double = jsonObject.getJSONObject("main").getDouble("temp") - 273.15
        var resultTemperature : Int = Math.rint(temp).toInt()

        weatherData.temperature = resultTemperature.toString()


       return weatherData

    }

    fun updateWeatherIcon(condition : Int) : String{

        if (condition >= 0 && condition < 300) {
            return "tstorm1"
        } else if (condition >= 300 && condition < 500) {
            return "light_rain"
        } else if (condition >= 500 && condition < 600) {
            return "shower3"
        } else if (condition >= 600 && condition <= 700) {
            return "snow4"
        } else if (condition >= 701 && condition <= 771) {
            return "fog"
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3"
        } else if (condition == 800) {
            return "sunny"
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2"
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3"
        } else if (condition == 903) {
            return "snow5"
        } else if (condition == 904) {
            return "sunny"
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3"
        }

        return "dunno"
    }

    fun cityTemperature() : String{
        return temperature
    }

    fun weatherIcon() : String{
        return weatherIcon
    }
    fun cityName() : String{
        return city
    }

}
