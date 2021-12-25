package com.example.fridaydubailottery.model



data class RandomTokens (
	val id : Int,
	val token_id : Int,
	val lottery_id : Int,
	val created_at : String,
	val updated_at : String,
	val token_name : String,
	val prize: String,
)