package com.applicationHybride.exercice02

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.spinnerVille)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val emplacement = parent.getItemAtPosition(position).toString().split(',')
//                Toast.makeText(this@MainActivity, "Vous avez choisi $emplacement", Toast.LENGTH_LONG).show() //Debug

                val queue = Volley.newRequestQueue(this@MainActivity)
                val url =
                    "https://api.weatherbit.io/v2.0/current?city=${emplacement[0]}&country=${emplacement[1]}&key=${
                        getString(R.string.weatherbit_key)
                    }"

                @Suppress("RedundantSamConstructor")
                val jsonRequest = JsonObjectRequest(
                    Request.Method.GET, //Méthode GET, PUT, POST, DELETE, etc.
                    url, //url de la ressource
                    null,
                    Response.Listener {
                        val data = it.getJSONArray("data").getJSONObject(0)
//                        Toast.makeText(this@MainActivity, "Température : ${data.getString("temp")}°C", Toast.LENGTH_LONG).show() //Debug
                        findViewById<TextView>(R.id.lblFetchedTemp).text = "${data.getString("temp")}°C"
                        findViewById<TextView>(R.id.lblFetchedSunrise).text = "${data.getString("sunrise")} UTC"
                        findViewById<TextView>(R.id.lblFetchedSunset).text = "${data.getString("sunset")} UTC"
                        findViewById<TextView>(R.id.lblFetchedPrecip).text = "${data.getString("precip")} mm"

                        val weatherIcon = data.getJSONObject("weather").getString("icon")
                        val imgWeather = findViewById<ImageView>(R.id.imgWeather)
                        val drawableResourceId: Int =
                            resources.getIdentifier(weatherIcon, "drawable", packageName)
                        imgWeather.setImageResource(drawableResourceId)
                    },
                    Response.ErrorListener {
                        //Traiter la réponse lorsqu'une erreur se produit (it contient erreur)
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    })
                queue.add(jsonRequest)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //rien
            }
        }
    }
}
