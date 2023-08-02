package com.project.rally_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StartTimerActivity : AppCompatActivity() {
    private lateinit var category: Category
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_timer)
        val vehiculoData = intent.getStringExtra("CATEGORY_ID")
        getCategoryFromApi(vehiculoData.toString())
    }
    private fun getCategoryFromApi(categoryId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.4:3002/") // Reemplaza con la URL de tu API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val categoryApiService = retrofit.create(CategoryApiService::class.java)

        val call = categoryApiService.getCategory(categoryId)
        call.enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if (response.isSuccessful) {
                    category = response.body() ?: Category("", null, null, null)
                    val txtWelcome = findViewById<TextView>(R.id.titleTextView)
                    txtWelcome.text = category.nombre
                } else {
                    println("Error en la respuesta de la API")
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                println("Error en la llamada a la API: ${t.message}")
            }
        })
    }
}