package com.project.rally_app

data class Race(
    val name: String,
    val categoria: List<Category>? = null,
    val vehiculo: List<Car>? = null,
    val _id: String? = null,
)
