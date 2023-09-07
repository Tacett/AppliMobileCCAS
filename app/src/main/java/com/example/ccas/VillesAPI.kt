package com.example.ccas

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface VillesAPI {
    @GET("city")
    suspend fun getAllVilles() : cities

    @GET("counter")
    suspend fun getSteps() : steps

    @PUT("counter/km")
    suspend fun addToCounter(@Body stepsToAdd : Counter)

}