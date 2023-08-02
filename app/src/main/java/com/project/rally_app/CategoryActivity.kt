package com.project.rally_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoryActivity : AppCompatActivity() {
    private lateinit var carrera: Race
    private lateinit var carreraId: String
    private lateinit var vehiculo: Array<Any>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val carrera = intent.getStringExtra(CarsActivity.EXTRA_CARRERA)
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        titleTextView.text = carrera
        carreraId = carrera.toString()
        getCarreraFromApi(carrera.toString())

        val saveButton = findViewById<Button>(R.id.addCategoryButton)
        saveButton.setOnClickListener {
            val categoryNameEditText = findViewById<EditText>(R.id.editTextCategory)
            val nombre = categoryNameEditText.text.toString()

            val category = Category(nombre, carrera, vehiculo) // Assuming 'carrera' is already initialized

            // Llamar al método para crear la categoría utilizando la API
            createCategory(category)

        }
    }
    private fun createCategory(category: Category) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.4:3002/") // Reemplaza con la URL de tu API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val categoryApiService = retrofit.create(CategoryApiService::class.java)

        val call = categoryApiService.createCategory(category)
        call.enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if (response.isSuccessful) {
                    // Manejar la respuesta exitosa de la API
                    println("Categoría creada exitosamente")
                    getCarreraFromApi(carreraId)
                } else {
                    println("Error en la respuesta de la API al crear la categoría")
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                // Manejar el error de la llamada a la API
                println("Error en la llamada a la API para crear la categoría: ${t.message}")
            }
        })
    }
    override fun onResume() {
        super.onResume()
        val carrera = intent.getStringExtra(CarsActivity.EXTRA_CARRERA)
        // Llamar al método para obtener la información de la carrera de la API
        // Esto se ejecutará cuando la actividad retome el foco después de regresar de otra actividad
        getCarreraFromApi(carrera.toString())
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
                    val categoryList = carrera.categoria?: emptyList()

                    val carList = carrera.vehiculo ?: emptyList()
                    vehiculo = arrayOf(carList)

                    // Configurar el RecyclerView
                    val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                    recyclerView.layoutManager = LinearLayoutManager(this@CategoryActivity)
                    recyclerView.adapter = CategoryAdapter(categoryList)

                } else {
                    println("Error en la respuesta de la API")
                }
            }

            override fun onFailure(call: Call<Race>, t: Throwable) {
                println("Error en la llamada a la API: ${t.message}")
            }
        })
    }
    class CategoryAdapter(private val categoryList: List<Category>) :
        RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

        class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txtCategory: TextView = itemView.findViewById(R.id.txtCategory)
            val btnSalida: Button = itemView.findViewById(R.id.btnSalida)
            val btnLlegada: Button = itemView.findViewById(R.id.btnLlegada)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category, parent, false)
            return CategoryViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            val currentCategory = categoryList[position]
            holder.txtCategory.text = currentCategory.nombre

            // Agregar la lógica para el botón de salida
            holder.btnSalida.setOnClickListener {
                // Navegar a StartTimerActivity y pasar el objeto currentCategory.vehiculo
                val intent = Intent(it.context, StartTimerActivity::class.java)
                intent.putExtra("CATEGORY_ID", currentCategory._id)
                it.context.startActivity(intent)
            }

            // Agregar la lógica para el botón de llegada
            holder.btnLlegada.setOnClickListener {
                // Navegar a FinishTimerActivity y pasar el objeto currentCategory.vehiculo
                val intent = Intent(it.context, FinishTimerActivity::class.java)
                intent.putExtra("VEHICULO_DATA", currentCategory.vehiculo)
                it.context.startActivity(intent)
            }
        }

        override fun getItemCount() = categoryList.size
    }

}
