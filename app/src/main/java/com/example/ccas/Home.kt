package com.example.ccas

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ProgressBar
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.TextField
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import kotlinx.coroutines.*
import kotlin.math.roundToInt

@Composable
fun Home (navController: NavController, viewModel : MainViewModel){
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxSize()) {
        Titre("Compteur de pas")
        Compteur(viewModel)
        Légende("km parcourus")
        Spacer(modifier = Modifier.height(15.dp))
        ProgressBar(viewModel)
        LégendeProgressBar("Progression jusqu'à ",viewModel)

    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(100.dp))
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            TextFieldView(viewModel)
            ChoixUnite(viewModel)
        }
        BoutonValider(viewModel)
        Spacer(modifier = Modifier.height(30.dp))
        Spacer(modifier = Modifier.height(30.dp))
    }
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
        BoutonNavigation(navController)
    }

}

@Composable
fun MonNom(nom: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = nom,
            style= MaterialTheme.typography.h4,
            modifier = Modifier.padding(5.dp),
            fontWeight = FontWeight.Bold)
        Text("Étudiant Ingénieur en 4ème année")
        Text("École d'ingénieur ISIS - INU Champollion")


    }
}

@Composable
fun Titre(nom: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = nom,
            style= MaterialTheme.typography.h5,
            modifier = Modifier.padding(20.dp),
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Compteur(viewModel : MainViewModel) {
    viewModel.getCounter()
    val steps by viewModel.steps.collectAsState()
    val roundedCounter = (steps * 10).roundToInt() / 10.0
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = roundedCounter.toString(),
            style= MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Légende(nom: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = nom,
            style= MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ProgressBar(viewModel : MainViewModel) {
    val steps by viewModel.steps.collectAsState()
    val cities by viewModel.cities.collectAsState()
    if(cities.isEmpty()) viewModel.getVilles()
    viewModel.findNearestCities(steps,cities)
    val progressToNext by viewModel.progressToNext.collectAsState()
    LinearProgressIndicator(progress = progressToNext, color = colorResource(id = R.color.Dark_Blue))
}

@Composable
fun LégendeProgressBar(nom: String,viewModel : MainViewModel) {
    val nextUnlockedCity by viewModel.nextUnlockedCity.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = nom + nextUnlockedCity,
            style= TextStyle(fontSize = 17.sp),
            fontWeight = FontWeight.Bold)
    }
}


@Composable
fun TextFieldView(viewModel : MainViewModel){
    val textState = remember { mutableStateOf("")  }
    TextField(
        value = textState.value,
        onValueChange = {
            textState.value = it
            viewModel.editStepsEntered(textState.value)
        },
        label = { Text(text = "Entrer le nombre de pas")},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        modifier = Modifier
            .fillMaxHeight(0.13f)
            .fillMaxWidth(0.65f)
        )
}


@Composable
fun BoutonNavigation(navController: NavController) {
    Button(onClick = {navController.navigate("Villes")},
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.CCAS_Blue)),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.15f))
    {
        Text("Consulter les villes débloquées",
            style= MaterialTheme.typography.h6,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
            )

    }
}



@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BoutonValider(viewModel: MainViewModel) {
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var addSteps = true

    if (showDialog) {
        val number = viewModel.stepsEntered.value.toFloat() // read the current value
        val unitSelected = viewModel.unitSelected.value
        addSteps = true
        if ((unitSelected == "pas" && number > 20000) || (unitSelected == "km" && number > 20)) {
            addSteps = false
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Attention !") },
                text = { Text("Chiffre trop grand !") },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }

    Button(
        onClick = {
            showDialog = true
            if (addSteps) {
                val unitSelected = viewModel.unitSelected.value
                val stepsToAdd = viewModel.stepsToAdd.value
                val stepsEntered = viewModel.stepsEntered.value

                isLoading = true
                CoroutineScope(Dispatchers.IO).launch {
                    if (unitSelected == "km") {
                        viewModel.addKm(stepsEntered)
                    } else {
                        viewModel.addSteps(stepsEntered)
                    }

                    viewModel.putSteps(viewModel.CounterToAdd.value)
                    delay(300)
                    viewModel.getCounter()

                    isLoading = false
                }
            }
        },
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.CCAS_Green))
    ) {
        Text(
            "Valider",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )
    }
}


@Composable
fun ChoixUnite(viewModel : MainViewModel){
    var expanded by remember { mutableStateOf(false)}
    val list = listOf("km","pas")
    var selectedItem by remember { mutableStateOf("pas")}
    var textFilledSize by remember { mutableStateOf(Size.Zero)}
    val icon = if(expanded) {
        Icons.Filled.KeyboardArrowUp
    }else{
        Icons.Filled.KeyboardArrowDown
    }

    Column(modifier = Modifier.padding(5.dp)) {

        OutlinedTextField(
            value = selectedItem,
            onValueChange = {
                            selectedItem=it
            },
            label= {Text(text= "")},
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
                .onGloballyPositioned { coordinates -> textFilledSize = coordinates.size.toSize() }
                .align(Alignment.End),
            enabled = false,
            trailingIcon = {
                Icon(icon,"",Modifier.clickable {expanded = !expanded})
            },

        )
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded= false },
            modifier = Modifier

                .width(with(LocalDensity.current){textFilledSize.width.toDp()})
            ) {
            list.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedItem = label
                    viewModel.changeUnitSelected(label)
                    Log.d("nouvelle unité",label);
                    expanded = false
                }) {
                    Text(text = label)
                }
        }
            }
        


    }



}
