package com.example.fridaydubailottery.model

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class PreviousLotteriesResponse{
	var previousLotteries: ArrayList<PreviousLotteries>? = null

}


data class PreviousLotteries (
	val id : Int,
	val name : String,
	val created_at : String,
	val updated_at : String,
	val date : String,
	val extra_time : String,
	val is_open : Int,
	val is_previous : Int,
	val random_tokens : List<RandomTokens>
)