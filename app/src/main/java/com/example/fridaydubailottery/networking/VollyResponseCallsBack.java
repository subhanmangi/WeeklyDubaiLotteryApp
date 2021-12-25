package com.example.fridaydubailottery.networking;

public interface VollyResponseCallsBack {
    void onVollySuccess(String result, int request_id);
    void onVollyError(String error, int request_id);
}
