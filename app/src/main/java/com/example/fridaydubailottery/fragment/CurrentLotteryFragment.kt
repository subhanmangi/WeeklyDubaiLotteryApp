package com.example.fridaydubailottery.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fridaydubailottery.R
import com.example.fridaydubailottery.adapter.DateTokenAdapter
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fridaydubailottery.model.CurrentLotteriesResponse
import com.example.fridaydubailottery.model.PreviousLotteriesResponse
import com.example.fridaydubailottery.networking.VollyResponseCallsBack
import com.example.fridaydubailottery.networking.VollyServerCom
import com.example.fridaydubailottery.utils.Constants.AUTHORIZATION
import com.example.fridaydubailottery.utils.Constants.CURRENT_LOTTERIES
import com.example.fridaydubailottery.utils.SharedPreference
import com.google.gson.Gson
import com.robinhood.ticker.TickerView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class CurrentLotteryFragment : Fragment(), VollyResponseCallsBack {
    var rvToken: RecyclerView? = null
    var dateTokenAdapter: DateTokenAdapter? = null
    var tvCounter: TextView? = null
    var tvLotteryDate: TextView? = null
    var tvDate: TextView? = null
    var tvFirstPrize: TickerView? = null
    var cl: ConstraintLayout? = null
    var clRemaining: ConstraintLayout? = null
    private var vollyServerCom: VollyServerCom? = null
    private val inputFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH)
    private val outputFormatter =
        DateTimeFormatter.ofPattern("EEE, MMM d, yyyy h:mm a", Locale.ENGLISH)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_current_lottery, container, false)
        rvToken = view.findViewById(R.id.rvToken)
        cl = view.findViewById(R.id.cl)
        clRemaining = view.findViewById(R.id.clRemaining)
        tvCounter = view.findViewById(R.id.tvCounter)
        tvDate = view.findViewById(R.id.tvDate)
        tvLotteryDate = view.findViewById(R.id.tvLotteryDate)
        tvFirstPrize?.setPreferredScrollingDirection(TickerView.ScrollingDirection.DOWN)


        tvFirstPrize = view.findViewById(R.id.tvFirstPrize)
        initObject()

        vollyServerCom!!.vollyAuthGETRequest(
            CURRENT_LOTTERIES,
            "current_lotteries",
            "Bearer " + SharedPreference.getSimpleString(requireContext(), AUTHORIZATION),
            0,
            "Please Wait",
            false
        )





        return view
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onVollySuccess(result: kotlin.String?, request_id: Int) {


        val currentLotteriesResponse =
            Gson().fromJson(result, CurrentLotteriesResponse::class.java)

        currentLotteriesResponse.currentLotteries?.let { it ->
            if (it.size != 0) {
//                val date = SimpleDateFormat("dd-MM-yyyy").parse("14-02-2018")
//                println(date.time)

                it[it.size - 1].let {
                    if (it.is_open == 0) {
                        cl?.visibility = View.GONE
                        tvCounter?.visibility = View.VISIBLE
                        clRemaining?.visibility = View.VISIBLE

                        val endDate =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").parse(it.date)
                        val createdDate =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").parse(it.created_date)

                        val format = SimpleDateFormat("MM/dd/yyyy h:mm a")

                        tvDate?.text =
                            "Lottery will be open on \n" + format.format(endDate!!).toString()

                        val difference = endDate.time - createdDate.time
                        startCounter(difference)

                    } else {
                        val date = LocalDateTime.parse(it.date, inputFormatter)
                        tvLotteryDate!!.text = outputFormatter.format(date)


                        cl?.visibility = View.VISIBLE
                        tvCounter?.visibility = View.GONE
                        clRemaining?.visibility = View.GONE

                        val tokenArrayList: ArrayList<String> = ArrayList()

                        it.random_tokens.forEach { tokens ->
                            if (tokens.prize == "2nd")
                                tokenArrayList.add(tokens.token_name)
                            else
                                tvFirstPrize?.text = tokens.token_name
                        }

                        dateTokenAdapter = DateTokenAdapter(
                            tokenArrayList, requireContext(), true
                        )

                        rvToken?.adapter = dateTokenAdapter
                    }
                }
            } else {
                cl?.visibility = View.GONE
                tvCounter?.visibility = View.VISIBLE
                clRemaining?.visibility = View.VISIBLE

                tvCounter?.text = "No Current Ticket"
            }
        }
    }

    override fun onVollyError(error: kotlin.String?, request_id: Int) {
        Toast.makeText(requireContext(), "There is some issue try again later", Toast.LENGTH_SHORT)
            .show()
    }

    private fun initObject() {
        vollyServerCom = VollyServerCom(context, this)
//        dateTokenAdapter = DateTokenAdapter(
//            listOf(
//                "123456",
//                "112233",
//                "123123"
//            ) as ArrayList<kotlin.String>, requireContext()
//        )
//        rvToken?.adapter = dateTokenAdapter
    }


    private fun startCounter(difference: Long) {
        object : CountDownTimer(difference, 1000) {
            @SuppressLint("DefaultLocale", "SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {

                val millis: Long = millisUntilFinished
                val hms = String.format(
                    "%02d:%02d:%02d:%02d",
                    TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toHours(millis) -
                            TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)),
                    (TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))),
                    (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millis)
                    ))

                )
                tvCounter!!.text = "Time remaining: $hms"

            }
            //here you can have your logic to set text to edittext

            override fun onFinish() {
                initObject()
                vollyServerCom!!.vollyAuthGETRequest(
                    CURRENT_LOTTERIES,
                    "current_lotteries",
                    "Bearer " + SharedPreference.getSimpleString(requireContext(), AUTHORIZATION),
                    0,
                    "Please Wait",
                    false
                )
            }
        }.start()
    }
}