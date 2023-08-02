package com.project.rally_app

data class Category(
    val nombre: String,
    val carrera: String? = null,
    val vehiculo: Array<Any>? = null,
    val _id: String? =null,
)
