package com.example.auctionclient.presentation.lot_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.auctionclient.domain.Bid
import com.example.auctionclient.ui.theme.Purple40

@Composable
fun LotDetailScreen(
    viewModel: LotDetailViewModel = viewModel(),
    navController: NavHostController,
    lotId: String
) {
    val lot = viewModel.lot
    val bids = viewModel.bids.filter { it.lotId == lot.id }

    val gradient = Brush.verticalGradient(
        0.0f to Purple40,
        1.0f to Color.White,
        startY = 0.0f,
        endY = 1600.0f
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(gradient),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = "Название: ${lot.title}")
                    Text(text = "Описание: ${lot.description}")
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Стартовая цена: ${lot.startPrice}", fontSize = 18.sp)
                        Text(text = "Текущая цена: ${lot.currentPrice}", fontSize = 18.sp)
                        Text(text = "Осталось времени: ${lot.endTime}", fontSize = 18.sp)
                    }

                }
            }
            Text(text = "Ставки:")
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                items(bids) { bid ->
                    BidView(bid)
                }
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .padding(10.dp),
                colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = Purple40,
                    disabledContainerColor = Purple40,
                    disabledContentColor = Purple40
                ),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(text = "Сделать ставку")
            }
        }
    }
}

@Composable
fun BidView(bid: Bid) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Purple40)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Сумма: ${bid.amount}", color = Color.White)
            Text(text = "Время: ${bid.timeStamp}", color = Color.White)
        }
    }
}