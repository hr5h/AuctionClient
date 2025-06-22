package com.example.auctionclient.presentation.lot_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.auctionclient.domain.Bid
import com.example.auctionclient.ui.theme.Purple40
import kotlin.math.roundToInt

@Composable
fun LotDetailScreen(
    viewModel: LotDetailViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val lot = viewModel.lot.collectAsState().value
    val bids = viewModel.bids.filter { it.lot.id == lot.id }
    val lotDetailState = viewModel.lotDetailState.collectAsState()
    val bidState = viewModel.bidState.collectAsState().value

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
                .padding(innerPadding)
                .background(gradient),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Название: ${lot.title}",
                            modifier = Modifier.padding(bottom = 4.dp, top = 10.dp),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        AsyncImage(
                            model = "https://masterpiecer-images.s3.yandex.net/5fab5867404521d:upscaled",
                            contentDescription = "imageLot",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(15.dp))
                        )
                    }
                    Text(
                        text = "Описание: ${lot.description}",
                        fontSize = 18.sp
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .align(Alignment.End)
                            .padding(top = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Стартовая цена: ${lot.startPrice.roundToInt()}",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .align(Alignment.Start)
                        )
                        Text(
                            text = "Текущая цена: ${lot.currentPrice.roundToInt()}",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .align(Alignment.Start)
                        )
                        Text(
                            text = "Время завершения: ${if(lot.endTime == "") "-" else lot.endTime}",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .align(Alignment.Start)
                        )
                    }
                }
            }
            Text(text = "Ставки:", color = Purple40, fontWeight = FontWeight.Bold, fontSize = 26.sp)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                items(bids) { bid ->
                    BidView(bid)
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        viewModel.showBid(true)
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
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
                if(lot.owner.username == viewModel.username) {
                    Button(
                        onClick = {
                            viewModel.finalizeLot()
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(10.dp),
                        colors = ButtonColors(
                            contentColor = Color.White,
                            containerColor = Purple40,
                            disabledContainerColor = Purple40,
                            disabledContentColor = Purple40
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text = "Завершить")
                    }
                }
            }
            if (lotDetailState.value.showBid) {
                BidBottomSheet(
                    onDismiss = { viewModel.showBid(false) },
                    bidState = bidState,
                    changeAmount = viewModel::changeAmountBid,
                    changeSelected = viewModel::changeSelected,
                    submitBid = viewModel::submitBid
                )
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
            Text(text = "Время: ${bid.timestamp}", color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BidBottomSheet(
    onDismiss: () -> Unit,
    bidState: BidState,
    changeAmount: (String) -> Unit,
    changeSelected: (BidAmount) -> Unit,
    submitBid: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = {
        onDismiss()
    }) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = bidState.amount,
                onValueChange = {
                    changeAmount(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple40,
                    unfocusedBorderColor = Purple40,
                    focusedTextColor = Purple40,
                    unfocusedTextColor = Purple40,
                    cursorColor = Purple40
                )
            )
            val chipList: List<BidAmount> = listOf(
                BidAmount.Min,
                BidAmount.Amount10,
                BidAmount.Amount20,
                BidAmount.Amount30,
                BidAmount.Amount40,
                BidAmount.Amount50,
                BidAmount.Amount100
            )
            ChipGroupCompose(
                chipList = chipList,
                selected = bidState.selected,
                changeSelected = changeSelected
            )
            Button(
                onClick = {
                    submitBid()
                },
                modifier = Modifier
                    .padding(20.dp),
                colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = Purple40,
                    disabledContainerColor = Purple40,
                    disabledContentColor = Purple40
                ),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(text = "Поставить")
            }
        }
    }
}

@Composable
fun ChipGroupCompose(
    chipList: List<BidAmount>,
    selected: BidAmount?,
    changeSelected: (BidAmount) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, top = 15.dp)
            .fillMaxWidth()
    ) {
        chipList.forEach { it ->
            Chip(
                title = it,
                selected = selected?.title ?: "",
                onSelected = {
                    changeSelected(it)
                }
            )
        }
    }
}

@Composable
fun Chip(
    title: BidAmount,
    selected: String,
    onSelected: (BidAmount) -> Unit
) {
    val isSelected = selected == title.title
    val background = if (isSelected) Purple40 else Color.White
    val contentColor = if (isSelected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .padding(end = 10.dp)
            .height(35.dp)
            .border(
                width = 1.dp,
                color = Color(0xFF6200EE),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .clickable {
                onSelected(title)
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title.title,
                color = contentColor,
                fontSize = 16.sp
            )
        }
    }
}