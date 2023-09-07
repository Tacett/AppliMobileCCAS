package com.example.ccas

import android.os.Bundle
import android.view.inputmethod.InputMethodSession
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.TextField
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TextInputSession
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage


@Composable
fun Villes(navController: NavController, viewModel : MainViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Titre2("Villes débloquées")
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            Row(horizontalArrangement = Arrangement.Center) {
                ListeVilles(viewModel,navController)
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            Row() {
                BoutonNavigation2(navController)
            }
        }
    }



@Composable
fun Titre2(nom: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = nom,
            style= MaterialTheme.typography.h5,
            modifier = Modifier.padding(28.dp),
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ListeVilles(viewModel : MainViewModel,navController: NavController){
    val cities by viewModel.cities.collectAsState()

    if(cities.isEmpty()) viewModel.getVilles()

    val steps by viewModel.steps.collectAsState()
    val matrix = ColorMatrix()
    matrix.setToSaturation(0F)


    LazyVerticalGrid(columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement =  Arrangement.spacedBy(10.dp)) {
        items(cities){
                city ->
            Card(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize()
                    .clickable(
                        onClick = {
                            if(steps>=city.distanceFrom0){
                            viewModel.editName(city.nom)
                            viewModel.editDescription(city.informations)
                            viewModel.editUrl(city.urlImg)
                            navController.navigate("ZoomVille")
                        }
                        }
                    ),
                elevation = 10.dp,
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if(steps>=city.distanceFrom0) {
                        AsyncImage(
                            model = city.urlImg,
                            contentDescription = "City miniature",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                        )
                    }
                    else{
                        AsyncImage(
                            model = city.urlImg,
                            contentDescription = "City miniature",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.colorMatrix(matrix)
                        )
                    }
                    Text(
                        text = city.nom,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }

}




@Composable
fun BoutonNavigation2(navController: NavController) {
    Button(onClick = {navController.navigate("Home")},
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.CCAS_Blue)),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.15f))
    {
        Text("Retour au menu principal",
            style= MaterialTheme.typography.h6,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )

    }
}
