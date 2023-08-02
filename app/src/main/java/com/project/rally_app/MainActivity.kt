package com.project.rally_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etCategory: EditText
    private lateinit var txtWelcome: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etName = findViewById(R.id.etName)

        val roundButton = findViewById<Button>(R.id.roundButton)
        roundButton.setOnClickListener {
            val name = etName.text.toString()
            api(name)
        }

    }

    fun api(name: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.4:3002/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val carreraApiService = retrofit.create(CarreraApiService::class.java)

        val carreraData = Race(name)

        val call = carreraApiService.createCarrera(carreraData)
        call.enqueue(object : retrofit2.Callback<Race> {
            override fun onResponse(call: Call<Race>, response: retrofit2.Response<Race>) {
                if (response.isSuccessful) {
                    val carrera = response.body()
                    if (carrera != null) {
                        val id = carrera._id

                        val txtWelcome = findViewById<TextView>(R.id.txtWelcome)
                        txtWelcome.text = id
                        val intent = Intent(this@MainActivity, CarsActivity::class.java)
                        // Pasar los datos a través del Intent
                        intent.putExtra(EXTRA_CARRERA_ID, id)
                        startActivity(intent)

                    } else {
                        println("La respuesta de la API es nula")
                    }
                } else {
                    println("Error en la respuesta de la API")
                }
            }

            override fun onFailure(call: Call<Race>, t: Throwable) {
                println("Error en la llamada a la API: ${t.message}")
            }
        })
    }
    companion object {
        const val EXTRA_CARRERA_ID = "extra_carrera_id"
    }
}
