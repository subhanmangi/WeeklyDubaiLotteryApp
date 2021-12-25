package com.example.fridaydubailottery.utils

import com.example.fridaydubailottery.utils.SharedPreference.saveSimpleString
import com.example.fridaydubailottery.utils.SharedPreference.saveBoolSharedPrefValue
import com.example.fridaydubailottery.model.UserModel
import android.app.Activity
import android.content.Context
import android.content.Intent

import com.example.fridaydubailottery.ui.LoginActivity

object Constants {
    var BASE_URL="https://engrswhb.com/weeklydubailottery.com/api/"
    var IS_LOGIN = "isLogin"
    var URI_LOGIN = "${BASE_URL}login"
    var URI_SIGNUP = "${BASE_URL}signup"
    var URI_CHANGE_PASSWORD = "${BASE_URL}change-password"
    var URI_FORGET_PASSWORD = "${BASE_URL}forgot-password"
    var CURRENT_LOTTERIES = "${BASE_URL}current/lotteries"
    var PREVIOUS_LOTTERIES = "${BASE_URL}previous/lotteries"
    var ADVERTISEMENT = "${BASE_URL}advertisements/links"

    var AUTHORIZATION = "Authorization"
    var EMAIL = "email"
    var ID = "id"
    var NAME = "name"

    var TOKEN_LIST="tokenList"
    var FIRST_PRIZE_TOKEN="firstPrizeToken"
    var TOTTERY_DATE="lotteryDate"
    private const val randomJobId = 1
    fun saveUser(context: Context?, userModel: UserModel) {
        saveSimpleString(context!!, ID, userModel.email)
        saveSimpleString(context, EMAIL, userModel.email)
        saveSimpleString(context, NAME, userModel.name)
        saveSimpleString(context, AUTHORIZATION, userModel.accessToken)
        saveBoolSharedPrefValue(context, IS_LOGIN, true)
    }

    fun logout(activity: Activity) {
        saveBoolSharedPrefValue(activity, IS_LOGIN, false)
        activity.startActivity(Intent(activity, LoginActivity::class.java))
        activity.finishAffinity()
    }
}