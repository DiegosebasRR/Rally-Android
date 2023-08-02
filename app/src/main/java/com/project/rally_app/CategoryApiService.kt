package com.project.rally_app

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CategoryApiService {
    @POST("/categoria/")
    fun createCategory(@Body category: Category): Call<Category>

    @GET("categoria/{id}")
    fun getCategory(@Path("id") categoryId: String): Call<Category>
}