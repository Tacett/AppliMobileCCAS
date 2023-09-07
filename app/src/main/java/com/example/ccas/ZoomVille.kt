package com.example.ccas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage


@Composable
fun ZoomVille (navController: NavController,viewModel : MainViewModel){

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxSize()) {
        MonImage(viewModel)
        NomVille(viewModel)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
        BoutonNavigation3(navController)
    }

}




@Composable
fun NomVille(viewModel : MainViewModel) {
    val cityName by viewModel.cityName.collectAsState()
    val cityDescription by viewModel.cityDescription.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = cityName,
            style= MaterialTheme.typography.h4,
            modifier = Modifier.padding(5.dp),
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(15.dp))
        Text(text= cityDescription,
            textAlign = TextAlign.Justify)

    }
}
@Composable
fun MonImage(viewModel : MainViewModel) {
    val cityPicture by viewModel.cityPicture.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        AsyncImage(
            model = cityPicture,
            contentDescription = "Translated description of what the image contains",
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .fillMaxHeight(0.4f)
                .fillMaxWidth()

        )
    }
}

@Composable
fun BoutonNavigation3(navController: NavController) {
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
