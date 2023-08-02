package com.project.rally_app

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CarreraApiService {
    @POST("carrera") // Reemplaza con la ruta real de la API para crear carreras
    fun createCarrera(@Body carrera: Race): Call<Race>
    @GET("carrera/{id}")
    fun getCarreraById(@Path("id") carreraId: String): Call<Race>
}