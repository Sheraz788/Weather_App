package com.sheraz.ali.weather_app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_change_city.*

class change_city : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_city)



        backButton.setOnClickListener { finish() }

        cityName.setOnEditorActionListener { textView, i, keyEvent ->

            val cityIntent = Intent(this, WeatherControllerActivity::class.java)
            cityIntent.putExtra("cityName", cityName.text.toString())
            startActivity(cityIntent)

            true
        }

    }
}
