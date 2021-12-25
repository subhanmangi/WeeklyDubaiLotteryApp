package com.example.fridaydubailottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fridaydubailottery.adapter.DateTokenAdapter
import com.example.fridaydubailottery.model.RandomTokens
import com.example.fridaydubailottery.utils.Constants.FIRST_PRIZE_TOKEN
import com.example.fridaydubailottery.utils.Constants.TOKEN_LIST
import com.example.fridaydubailottery.utils.Constants.TOTTERY_DATE
import com.robinhood.ticker.TickerView

class TokensActivity : AppCompatActivity() {
    var rvToken: RecyclerView?=null
    var tvFirstPrize: TickerView?=null
    var tvLotteryDate: TextView?=null
    var dateTokenAdapter: DateTokenAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokens)
        rvToken=findViewById(R.id.rvToken)
        tvFirstPrize=findViewById(R.id.tvFirstPrize)
        tvLotteryDate=findViewById(R.id.tvLotteryDate)
        tvFirstPrize?.setPreferredScrollingDirection(TickerView.ScrollingDirection.DOWN)



        val list =intent.getStringArrayListExtra((TOKEN_LIST))
        val firstPrize =intent.getStringExtra(FIRST_PRIZE_TOKEN)
        val date =intent.getStringExtra(TOTTERY_DATE)



        tvFirstPrize?.text=firstPrize
        tvLotteryDate?.text=date
        list?.remove(firstPrize)

        dateTokenAdapter= DateTokenAdapter(list!!
             ,this,true)

        rvToken?.adapter=dateTokenAdapter
    }
}