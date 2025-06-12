package com.example.auctionclient.presentation.lot_detail


data class BidState(
    val amount: String = "",
    val selected: BidAmount? = null,
)

sealed class BidAmount(val title: String) {

    data object Min: BidAmount("Min")
    data object Amount10: BidAmount("10%")
    data object Amount20: BidAmount("20%")
    data object Amount30: BidAmount("30%")
    data object Amount40: BidAmount("40%")
    data object Amount50: BidAmount("50%")
    data object Amount100: BidAmount("100%")
}