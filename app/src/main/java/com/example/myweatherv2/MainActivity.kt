package com.example.myweatherv2

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.loader.content.AsyncTaskLoader
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    val city: String = "Lahore, pk"
    val api: String= "d74f0465c0530062c3f60f4c5a24bc8b"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }

    inner class weatherTask(): AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errortext).visibility = View.GONE

        }


        override fun doInBackground(vararg p0: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$api").readText(
                        Charsets.UTF_8
                    )

            } catch (e: Exception) {
                response = null

            }

            return response
        }


        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val json_obj = JSONObject(result)
                val main = json_obj.getJSONObject("main")
                val sys = json_obj.getJSONObject("sys")
                val wind = json_obj.getJSONObject("wind")
                val weather = json_obj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long = json_obj.getLong("dt")
                val UpdatedAtText =
                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updatedAt * 1000)
                    )
                val temp = main.getString("temp") + "°C"
                val minTemp = "Min Temp: " + main.getString("temp_min") + "°C"
                val maxTemp = "Max Temp: " + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = json_obj.getString("name") + ", " + sys.getString("country")

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = UpdatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.humid).text = humidity
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.temp_min).text = minTemp
                findViewById<TextView>(R.id.temp_max).text = maxTemp
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.sunrise).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                findViewById<TextView>(R.id.sunset).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errortext).visibility = View.VISIBLE
            }
        }

    }

    }
