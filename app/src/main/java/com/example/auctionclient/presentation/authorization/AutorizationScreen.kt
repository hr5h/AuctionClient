package com.example.auctionclient.presentation.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.auctionclient.ui.theme.Purple40

@Composable
fun AuthorizationScreen(
    viewModel: AuthorizationViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val gradient = Brush.verticalGradient(
        0.0f to Purple40,
        1.0f to Color.White,
        startY = 0.0f,
        endY = 1600.0f
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(gradient),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    navController.navigate("login")
                },
                modifier = Modifier.padding(5.dp).fillMaxWidth(0.4f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Вход",
                    fontSize = 20.sp
                )
            }
            Button(
                onClick = {
                    navController.navigate("register")
                },
                modifier = Modifier.padding(5.dp).fillMaxWidth(0.4f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Регистрация",
                    fontSize = 20.sp
                )
            }
        }
    }
}