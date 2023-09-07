package com.example.ccas

data class cities(
    val cities: List<City> = listOf()
)

data class City(
    val distanceFrom0: Double = 0.0,
    val id: Int = 0,
    val informations: String = "",
    val nom: String = "",
    val urlImg: String = ""
)

data class steps(
    val valeur: Double = 0.0
)

data class Counter(val value: Float)

