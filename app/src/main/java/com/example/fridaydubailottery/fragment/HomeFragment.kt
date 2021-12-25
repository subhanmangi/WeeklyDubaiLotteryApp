package com.example.fridaydubailottery.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fridaydubailottery.R
import com.example.fridaydubailottery.adapter.ImageAdapter
import androidx.navigation.fragment.findNavController
import com.example.fridaydubailottery.model.AdvertismentResponse
import com.example.fridaydubailottery.networking.VollyResponseCallsBack
import com.example.fridaydubailottery.networking.VollyServerCom
import com.example.fridaydubailottery.utils.Constants
import com.example.fridaydubailottery.utils.SharedPreference
import com.google.gson.Gson


class HomeFragment : Fragment() , VollyResponseCallsBack {
    var adapter: ImageAdapter? = null
    var rvImage: RecyclerView? = null
    var btnCurrentLottery: Button? = null
    var btnPreviousLotteries: Button? = null
    private var vollyServerCom: VollyServerCom? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        rvImage = view.findViewById(R.id.rvImage)
        btnCurrentLottery = view.findViewById(R.id.btnCurrentLottery)
        btnPreviousLotteries = view.findViewById(R.id.btnPreviousLotteries)
        vollyServerCom = VollyServerCom(context, this)


        vollyServerCom!!.vollyAuthGETRequest(
            Constants.ADVERTISEMENT,
            "ADVERTISEMENT",
            "Bearer " + SharedPreference.getSimpleString(requireContext(), Constants.AUTHORIZATION),
            0,
            "Please Wait",
            false
        )





        btnCurrentLottery?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCurrentLotteryFragment())
        }

        btnPreviousLotteries?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPreviousLotteriesFragment())
        }

        return view
    }

    override fun onVollySuccess(result: String?, request_id: Int) {

        val advertismentResponse =
            Gson().fromJson(result, AdvertismentResponse::class.java)

        if(advertismentResponse.advertisements?.size!!>0){
            adapter =
                ImageAdapter(
                    advertismentResponse.advertisements!!
                        ,requireContext()
                )
            rvImage?.adapter = adapter
        }else{
            Toast.makeText(requireContext(), "There is no advertisement", Toast.LENGTH_SHORT)
                .show()
        }


    }

    override fun onVollyError(error: String?, request_id: Int) {
        Toast.makeText(requireContext(), "There is some issue try again later", Toast.LENGTH_SHORT)
            .show()
    }


}