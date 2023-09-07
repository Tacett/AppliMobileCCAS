package com.example.ccas

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.math.absoluteValue

class MainViewModel : ViewModel() {
    val cities = MutableStateFlow<List<City>>(listOf())
    val steps = MutableStateFlow<Double>(0.0)
    var cityName = MutableStateFlow("Rien")
    var cityDescription = MutableStateFlow("rien")
    var cityPicture = MutableStateFlow("")
    var progressToNext = MutableStateFlow<Float>(0F)
    var nextUnlockedCity = MutableStateFlow("")

    var stepsToAdd = MutableStateFlow<Float>(0F)
    val CounterToAdd = mutableStateOf(Counter(0f))

    val stepsEntered = MutableStateFlow("")
    val unitSelected = MutableStateFlow("pas")


    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.187.0.152:8080/api/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build();

    val service = retrofit.create(VillesAPI::class.java)


    fun getVilles() {
        viewModelScope.launch {
            cities.value = service.getAllVilles().cities
        }
    }

    fun getCounter() {
        viewModelScope.launch {
            steps.value = service.getSteps().valeur
        }
    }

    fun updateStepsToAdd() {
        stepsToAdd.value=stepsToAdd.value
        }

    fun putSteps(CounterToAdd: Counter) {
        viewModelScope.launch {
            service.addToCounter(CounterToAdd)
        }
    }

    fun editName(newValue: String) {
        cityName.value = newValue
    }

    fun editDescription(newValue: String) {
        cityDescription.value = newValue
    }

    fun editUrl(newValue: String) {
        cityPicture.value = newValue
    }

    fun editStepsEntered(newValue: String) {
        stepsEntered.value = newValue
    }

    fun changeUnitSelected(newValue: String) {
        unitSelected.value = newValue
    }

    fun addSteps(newValue: String) {
        stepsToAdd.value = newValue.toFloatOrNull() ?: 0F
        stepsToAdd.value = (stepsToAdd.value * 0.6 / 1000).toFloat()
        CounterToAdd.value= Counter(stepsToAdd.value)
    }

    fun addKm(newValue: String) {
        stepsToAdd.value = newValue.toFloatOrNull() ?: 0F
        CounterToAdd.value= Counter(stepsToAdd.value)
    }

    fun findNearestCities(distanceFrom0: Double, cities: List<City>) {
        var previousCity: City? = null
        var nextCity: City? = null
        var distanceRatio: Double? = 0.0

        for (i in cities.indices) {
            val currentCity = cities[i]

            if (currentCity.distanceFrom0 == distanceFrom0) {
                previousCity = if (i > 0) cities[i - 1] else null
                nextCity = if (i < cities.size - 1) cities[i + 1] else null
                break
            } else if (currentCity.distanceFrom0 > distanceFrom0) {
                nextCity = currentCity
                previousCity = if (i > 0) cities[i - 1] else null
                break
            } else {
                previousCity = currentCity
            }
        }
        if (previousCity != null && nextCity != null) {
            val distanceDiff = nextCity.distanceFrom0 - previousCity.distanceFrom0
            distanceRatio = if (distanceDiff == 0.0) {
                1.0
            } else {
                (distanceFrom0 - previousCity.distanceFrom0) / distanceDiff
            }
            progressToNext.value = distanceRatio.toFloat()
            nextUnlockedCity.value = nextCity.nom

        }
    }
}