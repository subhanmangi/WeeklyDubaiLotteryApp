package com.example.fridaydubailottery.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.fridaydubailottery.R
import com.example.fridaydubailottery.TokensActivity
import com.example.fridaydubailottery.adapter.DateTokenAdapter
import com.example.fridaydubailottery.model.PreviousLotteriesResponse
import com.example.fridaydubailottery.model.RandomTokens
import com.example.fridaydubailottery.networking.VollyResponseCallsBack
import com.example.fridaydubailottery.networking.VollyServerCom
import com.example.fridaydubailottery.ui.RecyclerItemClickListener
import com.example.fridaydubailottery.utils.Constants
import com.example.fridaydubailottery.utils.Constants.FIRST_PRIZE_TOKEN
import com.example.fridaydubailottery.utils.Constants.TOKEN_LIST
import com.example.fridaydubailottery.utils.Constants.TOTTERY_DATE
import com.example.fridaydubailottery.utils.SharedPreference
import com.google.gson.Gson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

@RequiresApi(Build.VERSION_CODES.O)
class PreviousLotteriesFragment : Fragment(), VollyResponseCallsBack {
    var rvDates: RecyclerView? = null
    var tvNoPreviousLotteries: TextView? = null
    var tvPreviousDates: TextView? = null

    var dateTokenAdapter: DateTokenAdapter? = null
    private var vollyServerCom: VollyServerCom? = null
    private var dateTokenListMap: HashMap<String, ArrayList<String>>? = HashMap()
    private var datesList: ArrayList<String>? = ArrayList()

    private val inputFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH)
    private val outputFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy h:mm a", Locale.ENGLISH)
    private var firstPrizeToken:ArrayList<String>?=ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_previous_lotteries, container, false)
        rvDates = view.findViewById(R.id.rvDates)
        tvPreviousDates = view.findViewById(R.id.tvPreviousDates)
        tvNoPreviousLotteries = view.findViewById(R.id.tvNoPreviousLotteries)
        initObject()

        vollyServerCom!!.vollyAuthGETRequest(
            Constants.PREVIOUS_LOTTERIES,
            "previous_lotteries",
            "Bearer " + SharedPreference.getSimpleString(requireContext(), Constants.AUTHORIZATION),
            0,
            "Please Wait",
            false
        )


        rvDates?.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                rvDates!!,
                object : RecyclerItemClickListener.OnItemClickListener {

                    override fun onItemClick(view: View, position: Int) {
                        activity?.let {
                            val intent = Intent(it, TokensActivity::class.java)
                            intent.putExtra(TOKEN_LIST, dateTokenListMap?.get(firstPrizeToken?.get(position))as ArrayList)
                            intent.putExtra(FIRST_PRIZE_TOKEN, firstPrizeToken?.get(position))
                            intent.putExtra(TOTTERY_DATE, datesList?.get(position))


                            it.startActivity(intent)
                        }
                    }

                    override fun onItemLongClick(view: View?, position: Int) {
                        TODO("do nothing")
                    }
                })
        )
        return view
    }

    override fun onVollySuccess(result: String?, request_id: Int) {
        val previousLotteriesResponse =
            Gson().fromJson(result, PreviousLotteriesResponse::class.java)

        previousLotteriesResponse.previousLotteries?.let { it ->
            if (it.size != 0) {
                tvNoPreviousLotteries?.visibility=View.GONE
                tvPreviousDates?.visibility=View.VISIBLE
                rvDates?.visibility=View.VISIBLE
                it.forEach {
                    val date = LocalDateTime.parse(it.date, inputFormatter)
                    datesList?.add(outputFormatter.format(date))
                    val tokenArrayList: ArrayList<String> = ArrayList()

                    it.random_tokens.forEach { tokens->
                        if(tokens.prize=="1st"){
                            firstPrizeToken?.add(tokens.token_name)
                        }else{
                            tokenArrayList.add(tokens.token_name)
                        }

                    }

                    dateTokenListMap?.put(firstPrizeToken!!.last(),tokenArrayList)

                }

                dateTokenAdapter = DateTokenAdapter(
                    datesList!!, requireContext(),false
                )

                rvDates?.adapter = dateTokenAdapter
            }else{
                tvNoPreviousLotteries?.visibility=View.VISIBLE
                tvPreviousDates?.visibility=View.GONE
                rvDates?.visibility=View.GONE
            }
        }


    }

    override fun onVollyError(error: String?, request_id: Int) {
        Toast.makeText(requireContext(), "There is some issue try again later", Toast.LENGTH_SHORT)
            .show()
    }

    private fun initObject() {
        vollyServerCom = VollyServerCom(context, this)

    }
}