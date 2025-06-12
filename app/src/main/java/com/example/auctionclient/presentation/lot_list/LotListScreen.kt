package com.example.auctionclient.presentation.lot_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.auctionclient.domain.Lot
import com.example.auctionclient.ui.theme.Purple40

@Composable
fun LotListScreen(
    navController: NavHostController,
) {
    val viewModel: LotListViewModel = viewModel()
    val lots = viewModel.lots

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Аукцион",
                modifier = Modifier.padding(15.dp),
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(lots) { lot ->
                    LotView(lot, navController)
                }
            }
        }
    }
}

@Composable
fun LotView(
    lot: Lot,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                navController.navigate("lot_detail/${lot.id}")
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.End),
                text = lot.title,
            )
            Row(
                Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.End)
                    .padding(top = 40.dp)
            ) {
                Text(
                    text = "Текущая цена:",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${lot.currentPrice}",
                    modifier = Modifier.wrapContentWidth(Alignment.End)
                )
            }
            Row(
                Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.End)
            ) {
                Text(
                    text = "Осталось времени:",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${lot.endTime}",
                    color = if (lot.endTime < 15f) Color.Red else Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.wrapContentWidth(Alignment.End)
                )
            }
        }
    }
}