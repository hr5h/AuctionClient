package com.example.auctionclient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.auctionclient.presentation.authorization.AuthorizationScreen
import com.example.auctionclient.presentation.authorization.login.LoginScreen
import com.example.auctionclient.presentation.lot_detail.LotDetailScreen
import com.example.auctionclient.presentation.lot_list.LotListScreen
import com.example.auctionclient.ui.theme.AuctionClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AuctionClientTheme {
                MyAppNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun MyAppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "authorization"
    ) {
        composable("authorization") {
            AuthorizationScreen(navController = navController)
        }
        composable("login") {
            LoginScreen(navController = navController, type = "login")
        }
        composable("register") {
            LoginScreen(navController = navController, type = "register")
        }
        composable("lot_list") {
            LotListScreen(navController = navController)
        }
        composable(
            route = "lot_detail/{lotId}",
            arguments = listOf(navArgument("lotId") { type = NavType.LongType })
            ) {
                LotDetailScreen(navController = navController)
            }
    }
}