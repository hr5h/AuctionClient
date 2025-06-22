package com.example.auctionclient.presentation.lot_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.auctionclient.domain.Lot
import com.example.auctionclient.ui.theme.Purple40
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Composable
fun LotListScreen(
    viewModel: LotListViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val lots = viewModel.lots
    val lotListState = viewModel.lotListState.collectAsState()
    val lotState = viewModel.lotState.collectAsState()

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
        var isRefreshing by remember { mutableStateOf(false) }
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true
                CoroutineScope(Dispatchers.Main).launch {
                    delay(500)
                    viewModel.getLots()
                    isRefreshing = false
                }
            },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    scale = true,
                    backgroundColor = Color.White,
                    contentColor = Purple40
                )
            }
        ) {
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
                Button(
                    onClick = {
                        viewModel.showDialogLot(true)
                    },
                    modifier = Modifier.padding(15.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple40,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Создать лот",
                    )
                }
            }
            if (lotListState.value.showDialogLot) {
                DialogLot(
                    onDismiss = { viewModel.showDialogLot(false) },
                    submitLot = viewModel::submitLot,
                    lotState = lotState,
                    changeTitle = viewModel::changeTitle,
                    changeDesciption = viewModel::changeDescription,
                    changeImageUrl = viewModel::changeImageUrl,
                    changeStartPrice = viewModel::changeStartPrice,
                    changeEndTime = viewModel::changeEndTime,
                )
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
                val jsonLot = Json.encodeToString(lot)
                navController.navigate("lot_detail/${jsonLot}")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {
            AsyncImage(
                model = "https://masterpiecer-images.s3.yandex.net/5fab5867404521d:upscaled",
                contentDescription = "imageLot",
                modifier = Modifier
                    .size(120.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.LightGray)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End),
                    text = lot.title,
                )
                Row(
                    Modifier
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
                        .align(Alignment.End)
                ) {
                    Text(
                        text = "Время завершения:",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${if(lot.endTime == "") "-" else lot.endTime}",
                        color = Purple40,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.wrapContentWidth(Alignment.End)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogLot(
    onDismiss: () -> Unit,
    submitLot: () -> Unit,
    lotState: State<LotState>,
    changeTitle: (String) -> Unit,
    changeDesciption: (String) -> Unit,
    changeImageUrl: (String) -> Unit,
    changeStartPrice: (String) -> Unit,
    changeEndTime: (String) -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight(0.52f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LotDialogTextField("Название:", lotState.value.title, changeTitle, false)
                LotDialogTextField("Описание:", lotState.value.description, changeDesciption, false)
                LotDialogTextField("Изображение:", lotState.value.imageUrl, changeImageUrl, false)
                LotDialogTextField(
                    "Стартовая цена:",
                    lotState.value.startPrice.toString(),
                    changeStartPrice,
                    true
                )
                LotDialogTextField(
                    "Время лота:",
                    lotState.value.endTime.toString(),
                    changeEndTime,
                    true
                )
                Button(
                    onClick = {
                        submitLot()
                    },
                    modifier = Modifier.padding(15.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple40,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Создать лот",
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotDialogTextField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    isNumber: Boolean,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        keyboardOptions = if (isNumber) KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ) else KeyboardOptions.Default,
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
            Text(text = label)
        }
    )
}