package com.example.auctionclient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.auctionclient.presentation.lot_detail.LotDetailScreen
import com.example.auctionclient.presentation.lot_list.LotListScreen
import com.example.auctionclient.ui.theme.AuctionClientTheme

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
        startDestination = "lot_list"
    ) {
        composable("lot_list") {
            LotListScreen(navController = navController)
        }
        composable("lot_detail/{lotId}") { backStackEntry ->
            val lotId = backStackEntry.arguments?.getString("lotId") ?: ""
            LotDetailScreen(navController = navController, lotId = lotId)
        }
    }
}