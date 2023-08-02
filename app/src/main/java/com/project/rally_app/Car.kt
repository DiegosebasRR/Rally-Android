package com.project.rally_app

data class Car(
    val numero: Int,
    val placa: String,
    val piloto: String,
    val dniPiloto: Int,
    val coPiloto: String,
    val dniCoPiloto: Int,
    val categoria: String,
    var imagenUrl: String,
    val carrera: String? =null,
    val _id: String? =null,
)

