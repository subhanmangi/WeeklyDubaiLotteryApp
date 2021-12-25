package com.example.fridaydubailottery.model

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class CurrentLotteriesResponse{
	var currentLotteries: ArrayList<CurrentLotteries>? = null

}


data class CurrentLotteries (
	val id : Int,
	val name : String,
	val created_at : String,
	val created_date : String,
	val updated_at : String,
	val date : String,
	val extra_time : String,
	val is_open : Int,
	val is_previous : Int,
	val random_tokens : List<RandomTokens>
)