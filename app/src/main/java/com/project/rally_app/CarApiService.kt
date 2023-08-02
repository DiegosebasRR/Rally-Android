package com.project.rally_app

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface CarApiService {
    @GET("/vehiculo/")
    fun getCars(): Call<List<Car>>

    @POST("/vehiculo/")
    fun createCar(@Body vehicle: Car): Call<Car>
}