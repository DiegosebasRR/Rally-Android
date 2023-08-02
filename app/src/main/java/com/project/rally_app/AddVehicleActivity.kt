package com.project.rally_app

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddVehicleActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var photoImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)
        val takePhotoButton: Button = findViewById(R.id.takePhotoButton)
        photoImageView = findViewById(R.id.photoImageView)
        takePhotoButton.setOnClickListener {
            // Llamar al intent de la cámara para capturar la foto
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

        val carrera = intent.getStringExtra(CarsActivity.EXTRA_CARRERA)
        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            // Obtener los datos ingresados por el usuario
            val vehicleNumberEditText = findViewById<EditText>(R.id.vehicleNumberEditText)
            val pilotNameEditText = findViewById<EditText>(R.id.pilotNameEditText)
            val plateNumberEditText = findViewById<EditText>(R.id.plateNumberEditText)

            val numero = vehicleNumberEditText.text.toString().toInt()
            val piloto = pilotNameEditText.text.toString()
            val placa = plateNumberEditText.text.toString()

            //otros datos para la api
            val dniPilotoEditText = findViewById<EditText>(R.id.dniPilotoEditText)
            val coPilotEditText = findViewById<EditText>(R.id.coPilotEditText)
            val dniCoPilotoEditText = findViewById<EditText>(R.id.dniCoPilotoEditText)
            val categoriaEditText = findViewById<EditText>(R.id.categoriaEditText)
            val imagenUrlEditText = findViewById<EditText>(R.id.imagenUrlEditText)

            val dniPiloto = dniPilotoEditText.text.toString().toInt()
            val coPiloto = coPilotEditText.text.toString()
            val dniCoPiloto = dniCoPilotoEditText.text.toString().toInt()
            val categoria = categoriaEditText.text.toString()
            val imagenUrl = imagenUrlEditText.text.toString()

            // Crear una instancia de Vehicle con los datos ingresados por el usuario
            val carData= Car(numero,placa,piloto,dniPiloto,coPiloto,dniCoPiloto,categoria,imagenUrl,carrera)

            // Realizar la solicitud POST a la API
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.4:3002/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val carApiService = retrofit.create(CarApiService::class.java)

            val call = carApiService.createCar(carData)
            call.enqueue(object : retrofit2.Callback<Car> {
                override fun onResponse(call: Call<Car>, response: retrofit2.Response<Car>) {
                    if (response.isSuccessful) {
                        // La solicitud POST se realizó con éxito, puedes hacer algo con la respuesta si lo deseas
                        val vehicle = response.body()
                        println("Vehículo creado con éxito: ${vehicle?.placa}")
                        finish()
                    } else {
                        println("Error en la respuesta de la API")
                    }
                }

                override fun onFailure(call: Call<Car>, t: Throwable) {
                    println("Error en la llamada a la API: ${t.message}")
                }
            })
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Obtener la imagen capturada desde el intent
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            // Mostrar la imagen en el ImageView
            photoImageView.setImageBitmap(imageBitmap)
        }
    }
}
