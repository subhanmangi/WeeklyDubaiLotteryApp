package com.example.fridaydubailottery.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.fridaydubailottery.MainActivity;
import com.example.fridaydubailottery.R;
import com.example.fridaydubailottery.model.JsonParseModel;
import com.example.fridaydubailottery.model.UserModel;
import com.example.fridaydubailottery.networking.VollyResponseCallsBack;
import com.example.fridaydubailottery.networking.VollyServerCom;
import com.example.fridaydubailottery.utils.Constants;
import com.example.fridaydubailottery.utils.Extension;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, VollyResponseCallsBack {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Context context;
    private VollyServerCom vollyServerCom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initObject();
        registerClicks();
    }

    private void initObject() {
        vollyServerCom = new VollyServerCom(context, this);
    }

    private void registerClicks() {
        btnLogin.setOnClickListener(this);
    }

    private void initView() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        context = LoginActivity.this;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
               String email = etEmail.getText().toString();
               String password = etPassword.getText().toString();

                if (Extension.isEmailValid(email, context) && Extension.isPasswordValid(password, context)) {
                    JSONObject loginUserObject = new JSONObject();
                    try {
                        loginUserObject.put("email", email);
                        loginUserObject.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    vollyServerCom.vollyPOSTRequest(Constants.INSTANCE.getURI_LOGIN(), loginUserObject, "request_tag", 0, "Logging", false);
                }
                break;
        }
    }

    @Override
    public void onVollySuccess(String result, int request_id) {
        try {

            UserModel userModel = JsonParseModel.getUser(result);
            Constants.INSTANCE.saveUser(context, userModel);


            startActivity(new Intent(context, MainActivity.class));
            finish();

        } catch (Exception e) {
            Extension.showToast("There is some issue please try again later", context);
            e.printStackTrace();
        }

    }

    @Override
    public void onVollyError(String error, int request_id) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(error);
            String errorMessage = jsonObject.getString("error");
            Extension.showToast(errorMessage, context);

        } catch (JSONException e) {
            e.printStackTrace();
            Extension.showToast("There is some issue please try again later", context);
        }
    }
}