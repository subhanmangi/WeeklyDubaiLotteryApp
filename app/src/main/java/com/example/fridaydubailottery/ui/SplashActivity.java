package com.example.fridaydubailottery.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fridaydubailottery.MainActivity;
import com.example.fridaydubailottery.R;
import com.example.fridaydubailottery.utils.Constants;
import com.example.fridaydubailottery.utils.SharedPreference;


public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = SplashActivity.this;
        boolean isLogin = SharedPreference.INSTANCE.getBoolSharedPrefValue(context, Constants.INSTANCE.getIS_LOGIN(), false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isLogin) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}