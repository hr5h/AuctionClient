package com.example.auctionclient.presentation.authorization.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.auctionclient.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val loginState = viewModel.loginState.collectAsState()

    val gradient = Brush.verticalGradient(
        0.0f to Purple40,
        1.0f to Color.White,
        startY = 0.0f,
        endY = 1600.0f
    )
    val context = LocalContext.current

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
            OutlinedTextField(
                value = loginState.value.login,
                onValueChange = { viewModel.changeLogin(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple40,
                    unfocusedBorderColor = Purple40,
                    focusedTextColor = Purple40,
                    unfocusedTextColor = Purple40,
                    cursorColor = Purple40,
                    focusedLabelColor = Purple40,
                    unfocusedLabelColor = Purple40
                ),
                label = {
                    Text(text = "Логин")
                }
            )
            OutlinedTextField(
                value = loginState.value.password,
                onValueChange = { viewModel.changePassword(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple40,
                    unfocusedBorderColor = Purple40,
                    focusedTextColor = Purple40,
                    unfocusedTextColor = Purple40,
                    cursorColor = Purple40,
                    focusedLabelColor = Purple40,
                    unfocusedLabelColor = Purple40
                ),
                label = {
                    Text(text = "Пароль")
                },
                visualTransformation = PasswordVisualTransformation(),
            )
            Button(
                onClick = {
                    viewModel.submitLogin { result ->
                        result.onSuccess {
                            navController.navigate("lot_list") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        }
                        result.onFailure { error ->
                            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                        }
                    }
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
        }
    }
}