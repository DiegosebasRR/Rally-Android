package com.project.rally_app

data class CarTimer(
    val numero: Int,
    val placa: String,
    val piloto: String,
    val dniPiloto: Int,
    val coPiloto: String,
    val dniCoPiloto: Int,
    val categoria: String,
    var imagenUrl: String,
    val llegada: String? =null,
    val salida: String? =null,
    val _id: String? =null,
)
