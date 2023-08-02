package com.project.rally_app


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarsActivity : AppCompatActivity() {

    private lateinit var carrera: Race

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars)

        val carreraId = intent.getStringExtra(MainActivity.EXTRA_CARRERA_ID)

        val addVehicleButton = findViewById<Button>(R.id.addVehicleButton)
        addVehicleButton.setOnClickListener {
            // Navegar a la AddVehicleActivity cuando se haga clic en el botón "Añadir vehículo"
            val intent = Intent(this, AddVehicleActivity::class.java)
            intent.putExtra(CarsActivity.EXTRA_CARRERA, carreraId)
            startActivity(intent)
        }

        val addCategoryButton = findViewById<Button>(R.id.addCategoryButton)
        addCategoryButton.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra(CarsActivity.EXTRA_CARRERA, carreraId)
            startActivity(intent)
        }


        // Mostrar el id en el TextView de CarsActivity
        val txtWelcome = findViewById<TextView>(R.id.titleTextView)
        txtWelcome.text = carreraId

        // Llamar al método para obtener la información de la carrera de la API
        getCarreraFromApi(carreraId.toString())
    }
    override fun onResume() {
        super.onResume()
        val carreraId = intent.getStringExtra(MainActivity.EXTRA_CARRERA_ID)
        // Llamar al método para obtener la información de la carrera de la API
        // Esto se ejecutará cuando la actividad retome el foco después de regresar de otra actividad
        getCarreraFromApi(carreraId.toString())
    }

    private fun getCarreraFromApi(carreraId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.4:3002/") // Reemplaza con la URL de tu API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val carreraApiService = retrofit.create(CarreraApiService::class.java)

        val call = carreraApiService.getCarreraById(carreraId)
        call.enqueue(object : Callback<Race> {
            override fun onResponse(call: Call<Race>, response: Response<Race>) {
                if (response.isSuccessful) {
                    carrera = response.body() ?: Race("", null, null, null)
                    val txtWelcome = findViewById<TextView>(R.id.titleTextView)
                    txtWelcome.text = carrera.name

                    // Obtener la lista de carros de la carrera, manejar si es nula
                    val carList = carrera.vehiculo ?: emptyList()

                    // Configurar el RecyclerView
                    val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                    recyclerView.layoutManager = LinearLayoutManager(this@CarsActivity)
                    recyclerView.adapter = CarAdapter(carList)
                } else {
                    println("Error en la respuesta de la API")
                }
            }

            override fun onFailure(call: Call<Race>, t: Throwable) {
                println("Error en la llamada a la API: ${t.message}")
            }
        })
    }

    // Adaptador para el RecyclerView
    class CarAdapter(private val carList: List<Car>) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

        class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val numberTextView: TextView = itemView.findViewById(R.id.txtNumber)
            val pilotTextView: TextView = itemView.findViewById(R.id.txtPilot)
            val plateTextView: TextView = itemView.findViewById(R.id.txtPlate)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false)
            return CarViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
            val currentCar = carList[position]
            holder.numberTextView.text = currentCar.numero.toString()
            holder.pilotTextView.text = currentCar.piloto
            holder.plateTextView.text = currentCar.placa
        }

        override fun getItemCount() = carList.size
    }

    companion object {
        const val EXTRA_CARRERA = "extra_carrera_id"
    }
}
