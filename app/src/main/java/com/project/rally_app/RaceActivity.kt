package com.project.rally_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race)
        // Llamada a la función para obtener todas las carreras
        getAllCarrerasFromApi()
    }
    private fun getAllCarrerasFromApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.4:3002/") // Reemplaza con la URL de tu API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val carreraApiService = retrofit.create(CarreraApiService::class.java)

        val call = carreraApiService.getAllCarreras()
        call.enqueue(object : Callback<List<Race>> {
            override fun onResponse(call: Call<List<Race>>, response: Response<List<Race>>) {
                if (response.isSuccessful) {
                    val carreraList = response.body() ?: emptyList()

                    // Ahora puedes hacer lo que necesites con la lista de carreras, por ejemplo, mostrarlas en un RecyclerView
                    val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                    recyclerView.layoutManager = LinearLayoutManager(this@RaceActivity)
                    recyclerView.adapter = RaceActivity.RaceAdapter(carreraList)

                } else {
                    println("Error en la respuesta de la API")
                }
            }

            override fun onFailure(call: Call<List<Race>>, t: Throwable) {
                println("Error en la llamada a la API: ${t.message}")
            }
        })
    }
    class RaceAdapter(private val raceList: List<Race>) :
        RecyclerView.Adapter<RaceAdapter.RaceViewHolder>() {

        class RaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txtRace: TextView = itemView.findViewById(R.id.txtRace)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_race, parent, false)
            return RaceViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RaceViewHolder, position: Int) {
            val currentRace = raceList[position]
            holder.txtRace.text = currentRace.name

            holder.txtRace.setOnClickListener{
                val intent = Intent(it.context, CarsActivity::class.java)
                intent.putExtra("race_id", currentRace._id)
                it.context.startActivity(intent)
            }
        }
        override fun getItemCount() = raceList.size
    }
}
